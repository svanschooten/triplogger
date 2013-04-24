package controllers;

import models.UserModel;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 3-4-13
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public class Test extends Controller {
    public static Result testBuddy() {
        List<UserModel> userModels = UserModel.all();
        for(UserModel u : userModels){
            u.setBuddies(UserModel.getBuddiesList(u));
            u.setPendingBuddies(UserModel.getPendingBuddiesList(u));
        }
        return ok(views.html.testBuddies.render(userModels));
    }

    public static Result testAddTrip() {
         return ok();
    }
}
