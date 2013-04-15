package models;


import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 1-4-13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class TripLink extends Model {

    @Id
    @GeneratedValue
    public int trid;

    public User tripper;
    public int tripperId;
    public Trip trip;
    public int tripId;
    public boolean validated;
    public boolean declined;

    public static Model.Finder<Integer, TripLink> find = new Model.Finder(Integer.class, TripLink.class);

    public TripLink(User tripper, Trip trip) {
        this.tripper = tripper;
        this.tripperId = tripper.uid;
        this.trip = trip;
        this.tripId = trip.tid;
        this.validated = false;
        this.declined = false;
    }

    public void create() {
        this.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<TripLink> all(){
        return find.all();
    }

    public void accept() {
        if(!this.declined) {
            this.validated = true;
            this.save();
        }
    }

    public void decline() {
        if(!this.validated){
            this.declined = true;
            this.save();
        }
    }

    public static void restoreTripBuddies(Trip trip) {
        List<TripLink> links = find.where().eq("tripId", trip.tid).findList();
        for (TripLink link : links) {
            trip.withBuddy.add(User.findById(link.tripperId));
        }
    }

    public static boolean trippedWithBuddy(Trip trip, User buddy){
        return TripLink.find.where().eq("tripId", trip.tid).eq("tripperId", buddy.uid).findUnique() == null;
    }

    public static List<TripLink> findTrips(User u) {
        return find.where().eq("tripperId", u.uid).findList();
    }
}
