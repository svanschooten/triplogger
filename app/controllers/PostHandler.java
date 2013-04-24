package controllers;

import models.Trip;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;
import views.html.signup;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 3-4-13
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class PostHandler extends Controller {

    public static Result authenticate() {
        Form<Application.Login> loginForm = Form.form(Application.Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("alias", loginForm.get().alias);
            session("success", "You've successfully logged in!");
            return redirect(
                    routes.Application.index()
            );
        }
    }

    public static Result adduser() {
        Form<UserModel> signupForm = Form.form(UserModel.class).bindFromRequest();
        if (signupForm.hasErrors()) {
            session("error", "Registration did not complete, please try again!");
            return badRequest(signup.render(signupForm));
        } else {
            session().clear();
            UserModel u = new UserModel(signupForm.get().alias, signupForm.get().email, signupForm.get().password);
            if(UserModel.find.where().eq("email", u.email).findUnique() != null) {
                session("error", "Registration did not complete, email is already in use!");
                return badRequest(signup.render(signupForm));
            } else if(UserModel.find.where().eq("alias", u.alias).findUnique() != null) {
                session("error", "Registration did not complete, alias is already in use!");
                return badRequest(signup.render(signupForm));
            } else{
                u.create();
                ValidateRequest vr = new ValidateRequest(u);
                Mailer.mailTo("Account creation for Triplogger", u.email, "noreply Triplogger <noreply@triplogger.com>", "",
                        "<p>Your account has been created, please verify with the following url:</p></br>" +
                                "<a href=\"localhost:9000" + routes.Application.userValidate(vr.token) + "\" >" +
                                routes.Application.userValidate(vr.token) + "</a>");
                ValidateRequest.create(vr);
                //TODO send mail with authentification link -> actual link + routes.Application.userValidate/vr.token
                session("success", "You've been registered, now validate your email!");
                return redirect(
                        routes.Application.index()
                );
            }
        }
    }
    @Security.Authenticated(Secured.class)
    public static Result addTrip() {
        Form<TripHead> tripForm = Form.form(TripHead.class);
        if(tripForm.hasErrors()) {
            return badRequest(views.html.index.render(new ArrayList<Drug>()));
        } else {
            TripHead t = tripForm.get();
            t.create();
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result addDrug() {
        Form<Drug> drugForm = Form.form(Drug.class);
        if(drugForm.hasErrors()) {
            return badRequest(views.html.index.render(new ArrayList<Drug>()));
        } else {
            Drug d = drugForm.get();
            d.create();
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result addMeasure() {
        Form<Measure> measureForm = Form.form(Measure.class);
        if(measureForm.hasErrors()) {
            return badRequest(views.html.index.render(new ArrayList<Drug>()));
        } else {
            Measure m = measureForm.get();
            m.create();
            return redirect(routes.Application.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteTrip(Integer id) {
        TripHead.delete(id);
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    public static Result requestBuddy(String targetAlias) {
        UserModel userModel = UserModel.findByAlias(session().get("alias"));
        UserModel target = UserModel.findByAlias(targetAlias);
        if (userModel.uid == target.uid) {
            return badRequest("You cannot add yourself as friend.");
        }
        if (userModel == null || target == null) {
            return badRequest("Users not found, are you supposed to do that.");
        } else {
            BuddyLink bl = BuddyLink.exists(userModel, target);
            if (bl != null) {
                return bl.validated ? badRequest("UserModel is already your buddy.")
                        : badRequest("UserModel is already requested as buddy.");
            }
            if (BuddyLink.exists(target, userModel) != null) {
                return badRequest("You are already requested by that userModel.");
            }
        }
        new BuddyLink(userModel, target).create();
        return ok("Pending buddy request to " + targetAlias);
    }

    @Security.Authenticated(Secured.class)
    public static Result acceptBuddy(String targetAlias) {
        UserModel userModel = UserModel.findByAlias(session().get("alias"));
        UserModel target = UserModel.findByAlias(targetAlias);
        if (userModel == null || target == null) {
            return badRequest("Users not found, are you supposed to do that.");
        } else {
            BuddyLink bl = BuddyLink.exists(target, userModel);
            if (bl == null) {
                return badRequest("Request not found, are you supposed to do that.");
            }
            bl.setValidated();
        }
        return ok("You are now buddies with " + targetAlias);
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelRequest(String targetAlias) {
        UserModel userModel = UserModel.findByAlias(session().get("alias"));
        UserModel target = UserModel.findByAlias(targetAlias);
        if (userModel == null || target == null) {
            return badRequest("Users not found, are you supposed to do that.");
        } else {
            BuddyLink bl = BuddyLink.exists(userModel, target);
            if (bl == null) {
                return badRequest("Request not found, are you supposed to do that.");
            }
            BuddyLink.delete(bl.blid);
            bl = BuddyLink.exists(userModel, target);
            if (bl != null) {
                return badRequest("Request still exists, are you supposed to do that.");
            }
        }
        return ok("Buddyrequest to " + targetAlias + " cancelled.");
    }

    @Security.Authenticated(Secured.class)
    public static Result getTripModal(String drug) {
        return ok(drug);
    }

    @Security.Authenticated(Secured.class)
    public static Result getTripBuddies(int tripId) {
        Trip trip = Trip.getFromTriphead(TripHead.findById(tripId), UserModel.findByAlias(session().get("alias")));
        return ok(views.html.tripbuddylist.render(trip.tripBuddies));
    }

}
