package controllers;

import play.api.templates.Html;
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
        return ok(stats.render(Html.apply("TODO")));
    }

    public static Result autoLogIn(String user , String pw) {
        return ok(index.render(Html.apply("TODO")));
    }

}
