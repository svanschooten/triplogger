package models;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 2-4-13
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class BuddyLink extends Model{

    @GeneratedValue
    private int id;

    @Id
    public int blid;

    public int userId;
    public int buddyId;
    public boolean validated;

    public static Model.Finder<Integer, BuddyLink> find = new Model.Finder(Integer.class, BuddyLink.class);

    public BuddyLink(UserModel userModel, UserModel buddy) {
        this.userId = userModel.uid;
        this.buddyId = buddy.uid;
        validated = false;
    }


    public void create() {
        this.blid = TLUID.create();
        this.save();
    }

    public static void delete(int id) {
        int buddyId = find.ref(id).buddyId;
        int userId = find.ref(id).userId;
        find.ref(id).delete();
        BuddyLink revBl = exists(UserModel.findById(buddyId), UserModel.findById(userId));
        if (revBl != null) {
            find.ref(revBl.blid).delete();
        }
    }

    public static List<BuddyLink> all(){
        return find.all();
    }

    public static List<BuddyLink> findBuddies(UserModel userModel) {
        return find.where().eq("userId", userModel.uid).eq("validated", true).findList();
    }

    public static List<BuddyLink> findPendingBuddies(UserModel userModel) {
        return find.where().eq("userId", userModel.uid).eq("validated", false).orderBy("blid desc").findList();
    }

    public static List<BuddyLink> getRequests(UserModel userModel) {
        return find.where().eq("buddyId", userModel.uid).eq("validated", false).orderBy("blid desc").findList();
    }

    public static BuddyLink exists(UserModel u, UserModel target) {
        return find.where().eq("userId", u.uid).eq("buddyId", target.uid).findUnique();
    }

    public void setValidated() {
        this.validated = true;
        this.create();
        BuddyLink reverse = new BuddyLink(UserModel.findById(buddyId), UserModel.findById(userId));
        reverse.validated = true;
        reverse.create();
    }

}
