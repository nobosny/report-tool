package controllers;

import controllers.helpers.Decoder;
import models.Params;
import models.Report;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import play.Play;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.BodyParser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Nobosny on 1/10/2015.
 * controllers
 */
public class ReportController extends Controller {

    public static Result postFilterReport() {
        DynamicForm form = Form.form().bindFromRequest();

        Params params = new Params();
        params.setStartDate(Decoder.dateStrDecoderOr(form.get("startDate"), null, true));
        params.setEndDate(Decoder.dateStrDecoderOr(form.get("endDate"), null, false));

        params.setMosquitoes(form.get("fmosquitoes") != null ? 1 : 0);
        params.setCockroaches(form.get("fcockroaches") != null ? 1 : 0);
        params.setMice(form.get("fmice") != null ? 1 : 0);
        params.setBiohazard(form.get("fbiohazard") != null ? 1 : 0);
        params.setTick(form.get("ftick") != null ? 1 : 0);

        params.setHouses(form.get("fhouses") != null ? 1 : 0);
        params.setBusinesses(form.get("fbusinesses") != null ? 1 : 0);
        params.setConstructionsites(form.get("fconstructions") != null ? 1 : 0);

        List<Report> query = Report.getFiltered(params);

        return ok(Json.toJson(query));
    }

    public static Result postNewReport() {
        DynamicForm form = Form.form().bindFromRequest();

        Http.MultipartFormData multi = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart file = multi.getFile("siteimage");

        Report report = new Report();
        report.recorddate = new Date();
        report.mosquitoes = form.get("mosquitoes") != null ? 1 : 0;
        report.cockroaches = form.get("cockroaches") != null ? 1 : 0;
        report.mice = form.get("mice") != null ? 1 : 0;
        report.biohazard = form.get("biohazard") != null ? 1 : 0;
        report.tick = form.get("tick") != null ? 1 : 0;
        report.othervectors = form.get("othervectors");

        report.houses = form.get("houses") != null ? 1 : 0;
        report.businesses = form.get("businesses") != null ? 1 : 0;
        report.constructionsites = form.get("constructionsites") != null ? 1 : 0;
        report.othersources = form.get("othersources");

        report.imagefile = saveUploadedFile(file);
        report.description = form.get("description");

        report.lat = Double.parseDouble(form.get("coords_lat"));
        report.lon = Double.parseDouble(form.get("coords_lon"));

        report.addeddate = new Date();

        try {
            report.save();
        } catch (Exception ex) {
            return badRequest("Error: " + ex.getMessage());
        }

        return ok("OK");
    }

    //REST API
    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result createReportAPI() {
        String message = "";
        try {
            Http.RequestBody body = request().body();
            String[] dateStr = body.asFormUrlEncoded().get("mosquitoes");

            Report report = new Report();
            report.recorddate = Decoder.dateDecoderOr(body.asFormUrlEncoded().get("recorddate"), new Date());
            report.mosquitoes = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("mosquitoes"), 0);
            report.cockroaches = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("cockroaches"), 0);
            report.mice = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("mice"), 0);
            report.biohazard = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("biohazard"), 0);
            report.tick = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("tick"), 0);
            report.othervectors = Decoder.stringDecoderOr(body.asFormUrlEncoded().get("othervectors"), "");

            report.houses = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("houses"), 0);
            report.businesses = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("businesses"), 0);
            report.constructionsites = Decoder.booleanIntDecoderOr(body.asFormUrlEncoded().get("constructions"), 0);
            report.othersources = Decoder.stringDecoderOr(body.asFormUrlEncoded().get("othersources"), "");

            report.imagefile = savePostedFile(Decoder.stringDecoderOr(body.asFormUrlEncoded().get("siteimage"), null));
            report.description = Decoder.stringDecoderOr(body.asFormUrlEncoded().get("description"), "");

            report.lat = Decoder.doubleDecoderOr(body.asFormUrlEncoded().get("coords_lat"), 0.0);
            report.lon = Decoder.doubleDecoderOr(body.asFormUrlEncoded().get("coords_lon"), 0.0);

            report.addeddate = new Date();

            report.save();
        } catch (Exception ex) {
            return ok(ex.getMessage());
        }

        return ok(message);
    }

    public static Result getPoints(Long time) {
        List<Report> reports = Report.findAllAfter(new Date(time));
        return ok(Json.toJson(reports));
    }

    public static Result getImage(Long rid) {
        Report report = Report.find.byId(rid);
        if (report == null) {
            return ok();
        } else {
            if (report.imagefile.length() > 10) {
                File imageFile = new File(report.imagefile);
                if (imageFile.exists()) {
                    return ok(imageFile);
                } else {
                    return ok();
                }
            } else {
                return ok();
            }
        }
    }

    private static String saveUploadedFile(Http.MultipartFormData.FilePart file) {
        if (file != null) {
            String fileName = "imagesite_server_" + new Date().getTime() + "_" + file.getFilename();
            File image = file.getFile();
            File newFile = new File(Play.application().path().getAbsolutePath() + "/uploads/siteimages", fileName);
            try {
                FileUtils.moveFile(image, newFile);
                return newFile.getAbsolutePath();
            } catch (Exception ex) {
                return "";
            }
        } else {
            return "";
        }
    }

    private static String savePostedFile(String fileStream) {
        if ((fileStream != null) && (fileStream != "")) {
            String fileName = "imagesite_client_" + new Date().getTime() + ".jpg";
            byte[] dearr = Base64.decodeBase64(fileStream);
            File newFile = new File(Play.application().path().getAbsolutePath() + "/uploads/siteimages", fileName);
            try {
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(dearr);
                fos.close();
                return newFile.getAbsolutePath();
            } catch (Exception ex) {
                return "";
            }
        } else {
            return "";
        }
    }
}
