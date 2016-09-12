var Singleton = new function() {
    this.date = new Date(0);
    this.firstLoad = true;
    this.openedModal = false;
    this.positionMarker = null;
    this.filtersApplied = false;
}

