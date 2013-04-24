package models;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 24-4-13
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 */
public class Trip {

    public int tid;
    public UserModel tripper;
    public Drug drug;
    public Date dfrom;
    public Date dtill;
    public int amount;
    public Measure measure;
    public List<UserModel> tripBuddies;
    public String comments;
    public boolean validated;

    public Trip (UserModel tripper, Drug drug, Date dfrom, Date dtill, int amount, Measure measure, List<UserModel> tripBuddies, String comments, boolean validated) {
        this.tripper = tripper;
        this.drug = drug;
        this.dfrom = dfrom;
        this.dtill = dtill;
        this.amount = amount;
        this.measure = measure;
        this.tripBuddies = tripBuddies;
        this.comments = comments;
        this.validated = validated;
    }

    public static Trip getFromTriphead(TripHead th, UserModel tripper) {
        ArrayList<UserModel> tripBuddies = new ArrayList<>();
        List<TripLink> triplinks = TripLink.findByHead(th);
        TripLink userTrip = null;
        for(TripLink tl : triplinks) {
            if(tl.tripperId == tripper.uid) {
                userTrip = tl;
            } else {
                tripBuddies.add(UserModel.findById(tl.tripperId));
            }
        }
        assert userTrip != null;
        Trip t = new Trip(tripper, Drug.findById(th.drugId), th.dfrom, th.dtill, userTrip.amount,
                Measure.findById(userTrip.measureId), tripBuddies, userTrip.comments, userTrip.validated);
        t.tid = th.tid;
        return t;
    }

    public static ArrayList<Trip> getTripsByUser(UserModel u){
        List<TripHead> userTripHeads = TripHead.getByUser(u);
        ArrayList<Trip> userTrips = new ArrayList<>();
        for(TripHead th : userTripHeads) {
            userTrips.add(Trip.getFromTriphead(th, u));
        }
        return userTrips;
    }

    public void create() {
        TripHead th = new TripHead(drug, dfrom, dtill);
        th.create();
        TripLink tl = new TripLink(tripper,th, amount, measure, comments);
        tl.accept();
        tl.create();
        for(UserModel tu : tripBuddies) {
            tl = new TripLink(tu,th, amount, measure, comments);
            tl.create();
        }
    }
}
