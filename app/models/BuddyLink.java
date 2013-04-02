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
    private int blid;

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
        find.ref(id).delete();
    }

    public static List<BuddyLink> all(){
        return find.all();
    }

    public static List<BuddyLink> findBuddies(User user) {
        return find.where().eq("userId",user.uid).findList();
    }

    public static List<BuddyLink> getRequests(User user) {
        return find.where().eq("buddyId", user.uid).eq("validated", false).findList();
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
