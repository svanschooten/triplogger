package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.libs.Crypto;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 1-4-13
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class BuddyRequest extends Model {


    @GeneratedValue
    @Id
    public int brid;

    public User from;
    public int fromId;
    public User target;
    public int targetId;

    public static Finder<Integer, BuddyRequest> find = new Finder(Integer.class, BuddyRequest.class);

    public BuddyRequest(User me, User him) {
        this.from = me;
        this.fromId = from.uid;
        this.target = him;
        this.targetId = target.uid;
    }

    public static void create(BuddyRequest br){
        br.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<BuddyRequest> all(){
        return find.all();
    }

    public static List<BuddyRequest> findFrom(User u) {
        return find.where().eq("from", u).findList();
    }

    public void accept() {
        from = User.findById(fromId);
        target = User.findById(targetId);
        from.addBuddy(target);
        from.save();
        target.addBuddy(from);
        target.save();
        this.delete();
    }

}
