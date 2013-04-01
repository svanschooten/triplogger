package models;


import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.libs.Crypto;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 1-4-13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class TripRequest extends Model {

    @Id
    @GeneratedValue
    public int trid;

    public User from;
    public int fromId;
    public User target;
    public int targetId;
    public Trip trip;
    public int tripId;

    public static Model.Finder<Integer, TripRequest> find = new Model.Finder(Integer.class, TripRequest.class);

    public TripRequest(User from, User target, Trip trip) {
        this.from = from;
        this.fromId = from.uid;
        this.target = target;
        this.targetId = target.uid;
        this.trip = trip;
        this.tripId = trip.tid;
    }

    public static void create(TripRequest request) {
        request.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<TripRequest> all(){
        return find.all();
    }

    public void accept() {
         //TODO implement triprequest accepting method.
    }
}
