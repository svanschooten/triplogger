package models;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 2-4-13
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class BuddyLink extends Model{

    @Id
    @GeneratedValue
    public int blid;

    public int userId;
    public int buddyId;
    public boolean validated;

    public static Model.Finder<Integer, BuddyLink> find = new Model.Finder(Integer.class, BuddyLink.class);

    public BuddyLink(User user, User buddy) {
        this.userId = user.uid;
        this.buddyId = buddy.uid;
        validated = false;
    }


    public static void create(BuddyLink bl) {
        bl.save();
    }

    public static void delete(int id) {
        int buddyId = find.ref(id).buddyId;
        int userId = find.ref(id).userId;
        find.ref(id).delete();
        BuddyLink revBl = exists(User.findById(buddyId), User.findById(userId));
        if (revBl != null) {
            find.ref(revBl.blid).delete();
        }
    }

    public static List<BuddyLink> all(){
        return find.all();
    }

    public static List<BuddyLink> findBuddies(User user) {
        return find.where().eq("userId",user.uid).eq("validated", true).findList();
    }

    public static List<BuddyLink> findPendingBuddies(User user) {
        return find.where().eq("userId",user.uid).eq("validated", false).orderBy("blid desc").findList();
    }

    public static List<BuddyLink> getRequests(User user) {
        return find.where().eq("buddyId", user.uid).eq("validated", false).orderBy("blid desc").findList();
    }

    public static BuddyLink exists(User u, User target) {
        return find.where().eq("userId", u.uid).eq("buddyId", target.uid).findUnique();
    }

    public void setValidated() {
        this.validated = true;
        create(this);
        BuddyLink reverse = new BuddyLink(User.findById(buddyId), User.findById(userId));
        reverse.validated = true;
        create(reverse);
    }

}
