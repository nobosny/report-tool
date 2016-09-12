package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.Date;

public class Application extends Controller {

    public static Date lastRequest;

    public static Result index() {
        return ok(index.render(Play.application().path().getAbsolutePath()));
    }

}
