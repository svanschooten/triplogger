package controllers;

import models.Trip;
import play.data.*;
import play.mvc.*;
import views.html.*;

import static play.data.Form.form;

public class Application extends Controller {
  
    public static Result index() {
        System.out.println("index");
        return ok(index.render());
    }

    public static Result profile() {
        System.out.println("profile");
        return ok(profile.render());
    }

    public static Result stats() {
        System.out.println("stats");
        return ok(stats.render());
    }

    public static Result logIn() {
        System.out.println("login");
        return ok(stats.render());
    }

    public static Result autoLogIn(String user , String pw) {
        System.out.println("autologin:" + user + " " + pw );
        return ok(index.render());
    }

    public static Result addTrip() {
        Form<Trip> tripForm = Form.form(Trip.class);
        if(tripForm.hasErrors()) {
            return badRequest(views.html.index.render());
        } else {
            Trip.create(tripForm.get());
            return redirect(routes.Application.index());
        }
    }

    public static Result deleteTrip(Integer id) {
        Trip.delete(id);
        return redirect(routes.Application.index());
    }

}
