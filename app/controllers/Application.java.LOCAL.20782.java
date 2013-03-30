package controllers;

import org.apache.commons.lang3.text.FormatFactory;
import org.h2.result.ResultColumn;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result profile() {
        return ok(profile.render("Your new application is ready."));
    }

    public static Result stats() {
        return ok(stats.render("Your new application is ready."));
    }

    public static Result logIn() {
        User user = Form.form(User.class).bindFromRequest().get();

    }
}
