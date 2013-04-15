package models;


import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.data.validation.Constraints.*;


/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 30-3-13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Trip extends Model{


    @GeneratedValue
    @Id
    public Integer tid;

    public static Finder<Integer, Trip> find = new Finder(Integer.class, Trip.class);

    public List<User> withBuddy = new ArrayList<>(  );

    public String comments;

    @Required
    public Drug drug;
    public int drugId;

    @Formats.DateTime(pattern="dd/MM/yyyy-hh:mm:ss")
    public DateTime dfrom;

    @Formats.DateTime(pattern="dd/MM/yyyy-hh:mm:ss")
    public DateTime dtill;

    @Constraints.Min(1)
    public int number;

    public Measure measure;
    public int measureId;

    public Trip(Drug drug, DateTime from, DateTime till, int number, Measure measure, String comments) {
        this.drug = drug;
        this.drugId = drug.did;
        this.dfrom = from;
        this.dtill = till;
        this.number = number;
        this.measure = measure;
        this.measureId = measure.mid;
        this.comments = comments;
        withBuddy = new ArrayList<>();
    }

    public Trip(Drug drug) {
        this.drug = drug;
        this.dfrom = new DateTime();
        this.dtill = new DateTime();
        this.number = 1;
        this.measure = Measure.findById(drug.standardMeasureId);
        this.comments = "";
        withBuddy = new ArrayList<>();
    }

    public void addTripper(User user) {
        withBuddy.add(user);
        TripLink tl = new TripLink(user, this);
        tl.accept();
        tl.create();
        this.save();
    }

    public void addBuddy(User user) {
        if(!withBuddy.contains(user)){
            withBuddy.add(user);
            TripLink tl = new TripLink(user, this);
            tl.create();
        }
        this.save();
    }

    public void create() {
        this.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<Trip> all(){
        return find.all();
    }

    public static Trip findById(int id){
        return find.where().eq("tid", id).findUnique();
    }

    public static List<Drug> findDrugsUsed(User u){
        if(u == null) {
            return new ArrayList<Drug>();
        }
        ArrayList<Drug> result = new ArrayList<>();
        List<TripLink> links = TripLink.findTrips(u);
        for(TripLink link : links) {
            Trip t = Trip.findById(link.tripId);
            result.add(Drug.findById(t.drugId));
        }
        return result;
    }

    public void restore() {
        withBuddy = new ArrayList<>();
        TripLink.restoreTripBuddies(this);
        drug = Drug.findById(drugId);
        measure = Measure.findById(measureId);
    }

    public void restoreId() {
        List<Trip> allTrips = Trip.all();
        int maxId = -1;
        for(Trip t : allTrips) {
            if(t.tid > maxId) {
                maxId = t.tid;
            }
        }
       tid = maxId;
    }

    public static List<Trip> getByUser(User u) {
        List<TripLink> links = TripLink.findTrips(u);
        List<Trip> result = new ArrayList<>();
        for(TripLink link : links) {
            Trip t = Trip.findById(link.tripId);
            t.restore();
            result.add(t);
        }
        return result;
    }
}
