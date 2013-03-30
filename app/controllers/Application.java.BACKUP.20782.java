package controllers;

import ch.qos.logback.classic.html.HTMLLayout;
import org.apache.commons.lang3.text.FormatFactory;
import org.h2.result.ResultColumn;
import models.*;
import play.api.templates.Html;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render(Html.apply("TODO")));
    }

    public static Result profile() {
        return ok(profile.render(Html.apply("TODO")));
    }

    public static Result stats() {
        return ok(stats.render(Html.apply("TODO")));
    }

    public static Result logIn() {
        User user = Form.form(User.class).bindFromRequest().get();
        return ok(stats.render(Html.apply("TODO")));
    }

    public static Result autoLogIn(String user , String pw) {
        return ok(stats.render(Html.apply("TODO")));
    }

}
