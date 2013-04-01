package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import views.html.*;


public class Application extends Controller{

    public static class Login {

        public String alias;
        public String password;

        public String validate() {
            if(User.authenticate(alias, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result profile() {
        return ok(profile.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result stats() {
        return ok(stats.render());
    }

    public static Result login() {
        User.init();
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result autoLogIn(String user , String pw) {
        User u = User.authenticate(user, pw);
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

    @Security.Authenticated(Secured.class)
    public static Result addTrip() {
        Form<Trip> tripForm = Form.form(Trip.class);
        if(tripForm.hasErrors()) {
            return badRequest(views.html.index.render());
        } else {
            Trip.create(tripForm.get());
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result addDrug() {
        Form<Drug> drugForm = Form.form(Drug.class);
        if(drugForm.hasErrors()) {
            return badRequest(views.html.index.render());
        } else {
            Drug.create(drugForm.get());
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result addMeasure() {
        Form<Measure> measureForm = Form.form(Measure.class);
        if(measureForm.hasErrors()) {
            return badRequest(views.html.index.render());
        } else {
            Measure.create(measureForm.get());
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteTrip(Integer id) {
        Trip.delete(id);
        return redirect(routes.Application.index());
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("alias", loginForm.get().alias);
            return redirect(
                    routes.Application.index()
            );
        }
    }

    public static Result logout() {
        session().clear();
        session("info", "You're now logged out.");
        return redirect(
                routes.Application.login()
        );
    }

    public static Result signup() {
        Form<User> signupForm = Form.form(User.class);
        return ok(signup.render(signupForm));
    }

    public static Result adduser() {
        Form<User> signupForm = Form.form(User.class).bindFromRequest();
        if (signupForm.hasErrors()) {
            session("error", "Registration did not complete, please try again!");
            return badRequest(signup.render(signupForm));
        } else {
            session().clear();
            User u = new User(signupForm.get().alias, signupForm.get().email, signupForm.get().password);
            if(User.find.where().eq("email", u.email).findUnique() != null) {
                session("error", "Registration did not complete, email is already in use!");
                return badRequest(signup.render(signupForm));
            } else if(User.find.where().eq("alias", u.alias).findUnique() != null) {
                session("error", "Registration did not complete, alias is already in use!");
                return badRequest(signup.render(signupForm));
            } else{
                User.create(u);
                ValidateRequest vr = new ValidateRequest(u);
                ValidateRequest.create(vr);
                Mailer.mailTo("Account creation for Triplogger", u.email, "noreply Triplogger <noreply@triplogger.com>", "",
                        "<p>Your account has been created, please verify with the following url:</p></br>" +
                        "<a href=\"localhost:9000" + routes.Application.userValidate(vr.token) + "\" >" +
                                routes.Application.userValidate(vr.token) + "</a>");
                //TODO send mail with authentification link -> actual link + routes.Application.userValidate/vr.token
                session("success", "You've been registered, now validate your email!");
                return redirect(
                        routes.Application.index()
                );
            }
        }
    }

    public static Result userValidate(String token) {
        ValidateRequest vr = ValidateRequest.findRequest(token);
        if(vr == null || vr.targetId < 0) {
            return badRequest(views.html.p404.render());
        } else {
            vr.validate();
            session("success", "User validated, you can now log in!");
            return redirect(
                    routes.Application.index()
            );
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result advancedtrip() {
        return ok(views.html.advancedTrip.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result buddylist() {
        return ok(views.html.buddyList.render());
    }
}
