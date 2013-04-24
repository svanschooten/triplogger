package controllers;

import models.*;
import play.Routes;
import play.data.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;


public class Application extends Controller{

    public static class Login {

        public String alias;
        public String password;

        public String validate() {
            if(UserModel.authenticate(alias, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render(TripHead.findDrugsUsed(UserModel.findByAlias(session().get("alias")))));
    }

    @Security.Authenticated(Secured.class)
    public static Result profile(String alias) {
        return ok(profile.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result stats() {
        return ok(stats.render());
    }

    public static Result login() {
        UserModel.init();
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result autoLogIn(String user , String pw) {
        UserModel u = UserModel.authenticate(user, pw);
        if(u != null){
            session().clear();
            session("alias", u.alias);
            session("success", "You've successfully logged in!");
            return redirect(
                    routes.Application.index()
            );
        } else {
            session("error", "Invalid user or password");
            Form<Login> loginForm = Form.form(Login.class);
            return badRequest(login.render(loginForm));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result addLocalTrip() {
        return ok(tripform.render());
    }

    public static Result logout() {
        session().clear();
        session("info", "You're now logged out.");
        return redirect(
                routes.Application.login()
        );
    }

    public static Result signup() {
        Form<UserModel> signupForm = Form.form(UserModel.class);
        return ok(signup.render(signupForm));
    }

    public static Result userValidate(String token) {
        ValidateRequest vr = ValidateRequest.findRequest(token);
        if(vr == null || vr.targetId < 0) {
            return badRequest(views.html.p404.render());
        } else {
            vr.validate();
            session("success", "UserModel validated, you can now log in!");
            return redirect(
                    routes.Application.index()
            );
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result trips() {
        UserModel u = UserModel.findByAlias(session().get("alias"));
        u.restore();
        return ok(views.html.trips.render(u.trips));
    }

    @Security.Authenticated(Secured.class)
    public static Result buddylist() {
        UserModel u = UserModel.findByAlias(session().get("alias"));
        u.restore();
        return ok(views.html.buddylist.render(u));
    }

    @Security.Authenticated(Secured.class)
    public static Result getUserAutocomplete(String term) {
        List<UserModel> userModels = UserModel.findLikeAlias(term);
        ArrayList<String> aliases = new ArrayList<>();
        for(UserModel u : userModels){
            aliases.add(u.alias);
        }
        return ok(Json.toJson(aliases));
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        // Routes
                        controllers.routes.javascript.PostHandler.requestBuddy(),
                        controllers.routes.javascript.PostHandler.acceptBuddy(),
                        controllers.routes.javascript.PostHandler.cancelRequest(),
                        controllers.routes.javascript.Application.profile()
                )
        );
    }
}
