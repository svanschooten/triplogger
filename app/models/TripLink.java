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

    public User from;
    public int fromId;
    public User target;
    public int targetId;
    public Trip trip;
    public int tripId;
    public boolean validated;

    public static Model.Finder<Integer, TripLink> find = new Model.Finder(Integer.class, TripLink.class);

    public TripLink(User from, User target, Trip trip) {
        this.from = from;
        this.fromId = from.uid;
        this.target = target;
        this.targetId = target.uid;
        this.trip = trip;
        this.tripId = trip.tid;
    }

    public static void create(TripLink request) {
        request.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<TripLink> all(){
        return find.all();
    }

    public void accept() {
        from = User.findById(fromId);
        target = User.findById(targetId);
        trip = Trip.findById(tripId);
        trip.addBuddy(target);
        Trip buddyTrip = new Trip(target, trip.drug, trip.dfrom, trip.dtill, trip.number, trip.measure, trip.comments);
        buddyTrip.addBuddy(from);
        buddyTrip.save();
        this.delete();
    }
}
