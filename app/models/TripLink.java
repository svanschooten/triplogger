package models;


import play.data.validation.Constraints;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 1-4-13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class TripLink extends Model {

    @GeneratedValue
    private int id;

    @Id
    public int trid;

    public UserModel tripper;
    public int tripperId;
    public TripHead trip;
    public int tripId;
    public boolean validated;
    public boolean declined;

    @Constraints.Min(1)
    public int amount;

    public Measure measure;
    public int measureId;
    public String comments;

    public static Model.Finder<Integer, TripLink> find = new Model.Finder(Integer.class, TripLink.class);

    public TripLink(UserModel tripper, TripHead trip, int amount, Measure measure, String comments) {
        this.tripper = tripper;
        this.tripperId = tripper.uid;
        this.trip = trip;
        this.tripId = trip.tid;
        this.amount = amount;
        this.measure = measure;
        this.measureId = measure.mid;
        this.comments = comments;
        this.validated = false;
        this.declined = false;
    }

    public void create() {
        this.trid = TLUID.create();
        this.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<TripLink> all(){
        return find.all();
    }

    public boolean accept() {
        if(!this.declined) {
            this.validated = true;
            this.save();
            return true;
        }
        return false;
    }

    public boolean decline() {
        if(!this.validated){
            this.declined = true;
            this.save();
            return true;
        }
        return false;
    }

    public static boolean trippedWithBuddy(TripHead trip, UserModel buddy){
        return TripLink.find.where().eq("tripId", trip.tid).eq("tripperId", buddy.uid).findUnique() == null;
    }

    public static List<TripLink> findTrips(UserModel u) {
        return find.where().eq("tripperId", u.uid).eq("declined", false).findList();
    }

    public static List<TripLink> findByHead(TripHead th) {
        return TripLink.find.where().eq("tripId", th.tid).findList();
    }
}
