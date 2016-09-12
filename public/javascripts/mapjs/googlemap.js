/**
 * Created by Nobosny on 1/8/2015.
 */

var map;
var openedInfoWindow;
var markerCluster;
var markersArray = [];

function initialize() {
    var mapOptions = {
        center: { lat: 0, lng: 0},
        zoom: 2,
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        },
    };
    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    getLocation();

    var controlDiv = $("#div_newreport");
    var controlBtn = $("#btn_newreport");
    controlDiv.index = 0;
    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(controlDiv[0]);

    var controlDivLoc = $("#div_mylocation");
    var controlBtnLoc = $("#btn_mylocation");
    controlDivLoc.index = 0;
    map.controls[google.maps.ControlPosition.RIGHT_TOP].push(controlDivLoc[0]);

    markerCluster = new MarkerClusterer(map, []);

    $.get( "/getpoints/" + 0, function( data ) {
        Singleton.date = new Date();
        addMarkers(data);
    });

}

function getLocation() {
    // Try HTML5 geolocation
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = new google.maps.LatLng(position.coords.latitude,
                position.coords.longitude);

            if (Singleton.positionMarker == null) {
                Singleton.positionMarker = new google.maps.Marker({
                    position: pos,
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        scale: 5,
                        strokeColor: '#800808',
                        strokeWeight: 1,
                        fillColor: 'red',
                        fillOpacity: 1.0
                    },
                    map: map
                });
            } else {
                Singleton.positionMarker.setPosition(pos);
            }

            if (Singleton.firstLoad) {
                map.setCenter(pos);
                map.setZoom(14);
                Singleton.firstLoad = false;
            }

            if (!Singleton.openedModal) {
                $("#coords_lat").val(position.coords.latitude);
                $("#coords_lon").val(position.coords.longitude);
            }

        }, function() {
            handleNoGeolocation(true);
        });
    } else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
    }
}

function handleNoGeolocation(errorFlag) {
    if (errorFlag) {
        var content = 'Error: The Geolocation service failed.';
    } else {
        var content = 'Error: Your browser doesn\'t support geolocation.';
    }

    if (Singleton.firstLoad) {
        var options = {
            map: map,
            position: new google.maps.LatLng(0, 0),
            content: content
        };

        var infowindow = new google.maps.InfoWindow(options);
        map.setCenter(options.position);
        Singleton.firstLoad = false;
    }
}

function addMarkers(data) {
    var i = 0;
    var len = data.length;
    var bounds = new google.maps.LatLngBounds();
    var infoWindow = new google.maps.InfoWindow();
    for (; i < len; i++) {
        var date = new Date(data[i].recorddate);
        var mymarker = new google.maps.Marker({
            position: new google.maps.LatLng(data[i].lat, data[i].lon),
            //map: map,
            animation: google.maps.Animation.DROP,
            rid: data[i].rid,
            recorddate: date.toLocaleString()
        });

        google.maps.event.addListener(mymarker, 'click', function() {
            infoWindow.close();
            infoWindow.setContent('<strong><p>Added on: ' + this.recorddate + '</p></strong>'
            +'<img src="/image/' + this.rid + '" width="300px">'
            );
            map.setCenter(this.position);
            infoWindow.open(map, this);
        });

        markerCluster.addMarker(mymarker);
        bounds.extend(mymarker.position);
        markersArray.push(mymarker);
    }

    //Check for nearest points and send the notification
    checkDangerZone(Singleton.positionMarker, 100);

    return bounds;
}

// center should be Singleton.positionMarker
function checkDangerZone(center, radiusInMtrs) {

    if (center == null) {
        return;
    }

    var count = 0;
    for (var i=0; i < markersArray.length; i++) {
        if (google.maps.geometry.spherical.computeDistanceBetween(markersArray[i].position, center.position) < radiusInMtrs) {
            count++;
        }
    }

    if (count <= 3) {
        //alert("BAJA");
            $('#status').removeClass().addClass('label label-info').text('LOW RISK');
    } else {
        if ((count >= 4) && (count <= 6)) {
            //alert("MEDIA");
            $('#status').removeClass().addClass('label label-warning').text('MEDIUM RISK');
        } else {
            if (count > 6) {
                //alert("ALTA");
                $('#status').removeClass().addClass('label label-danger').text('HIGH RISK');
            }
        }
    }
}

google.maps.event.addDomListener(window, 'load', initialize);