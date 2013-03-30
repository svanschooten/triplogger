package controllers;

import play.api.templates.Html;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render());
    }

    public static Result profile() {
        return ok(profile.render());
    }

    public static Result stats() {
        return ok(stats.render());
    }

    public static Result logIn() {
        return ok(stats.render());
    }

    public static Result autoLogIn(String user , String pw) {
        return ok(index.render());
    }

}
