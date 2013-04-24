package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.libs.Crypto;


/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 30-3-13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class UserModel extends Model{

    public static Finder<Integer, UserModel> find = new Finder(Integer.class, UserModel.class);

    @GeneratedValue
    private int id;

    @Id
    public int uid;
    public String alias;
    public String email;

    public int trippoints;
    public String password;


    public List<Trip> trips = new ArrayList<>();
    public List<UserModel> buddies = new ArrayList<>();
    public List<UserModel> pendingBuddies = new ArrayList<>();
    public List<UserModel> requests = new ArrayList<>();
    public boolean validated;

    public UserModel(String alias, String email, String password) {
        this.alias = alias;
        this.email = email;
        this.password = Crypto.encryptAES(password);
        trippoints = 0;
        validated = false;
    }

    public static void init() {
        if(authenticate("svsoke", "password")== null && authenticate("mcawesome", "password")== null) {
            UserModel stijn = new UserModel("svsoke", "svsoke@hotmail.com", "password");
            UserModel elgar = new UserModel("mcawesome", "elgar.groot@gmail.com", "password");
            stijn.validated = true;
            elgar.validated = true;
            stijn.create();
            elgar.create();
            BuddyLink bl = new BuddyLink(stijn, elgar);
            bl.setValidated();
            bl.create();
        }
        if(TripHead.findDrugsUsed(UserModel.findByAlias("svsoke")).isEmpty()) {
            if(Drug.all().size() == 0 ) {
                if(Measure.all().size() == 0) {
                    Measure m = new Measure("units","units");
                    m.create();
                    for(int i = 0; i < 30; i++){
                        Measure m2 = new Measure("test", i+"");
                        m2.create();
                    }
                }
                Drug d = new Drug("Shrooms","http://www.erowid.org/plants/mushrooms/mushrooms.shtml", Measure.findName("units"));
                d.create();
            }
            ArrayList<UserModel> buds = new ArrayList<>();
            buds.add(UserModel.findByAlias("mcawesome"));
            Trip t = new Trip(UserModel.findByAlias("svsoke"), Drug.findByName("Shrooms"), new Date(), new Date(),
                    2, Measure.findName("units"), buds, "Yeah, tripping!!", true);
            t.create();
            TripLink.find.where().eq("tripperId", buds.remove(0).uid).findUnique().accept();
        }
    }

    public void setBuddies(ArrayList<UserModel> buddyList) {
        this.buddies = buddyList;
    }

    public void setPendingBuddies(ArrayList<UserModel> buddyList) {
        this.pendingBuddies = buddyList;
    }

    public static UserModel findById(int id){
        return find.where().eq("uid", id).findUnique();
    }

    public static UserModel findByAlias(String alias){
        return find.where().eq("alias", alias).findUnique();
    }

    public static List<UserModel> findLikeAlias(String alias){
        return find.where().ilike("alias", "%" + alias + "%").findList();
    }

    public static ArrayList<UserModel> getBuddiesList(UserModel u) {
        List<BuddyLink> budds = BuddyLink.findBuddies(u);
        ArrayList<UserModel> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(UserModel.findById(bl.buddyId));
        }
        return buddies;
    }

    public static ArrayList<UserModel> getPendingBuddiesList(UserModel u) {
        List<BuddyLink> budds = BuddyLink.findPendingBuddies(u);
        ArrayList<UserModel> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(UserModel.findById(bl.buddyId));
        }
        return buddies;
    }

    public static ArrayList<UserModel> getRequestsList(UserModel u) {
        List<BuddyLink> budds = BuddyLink.getRequests(u);
        ArrayList<UserModel> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(UserModel.findById(bl.userId));
        }
        return buddies;
    }

    public void getBuddies() {
        this.buddies = getBuddiesList(this);
    }

    public void getPendingBuddies() {
        this.pendingBuddies = getPendingBuddiesList(this);
    }

    public void getRequests() {
        this.requests = getRequestsList(this);
    }

    public void addBuddy(UserModel u) {
        if(BuddyLink.exists(this, u) == null) {
            new BuddyLink(this, u).create();
        }
    }

    public void addPoints(int p) {
        this.trippoints += p;
        this.save();
    }

    public void create() {
        this.uid = TLUID.create();
        this.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<UserModel> all(){
        return find.all();
    }

    public static UserModel authenticate(String alias, String password) {
        return find.where().eq("alias", alias).eq("password", Crypto.encryptAES(password)).eq("validated", true).findUnique();
    }

    public void restore() {
        this.getBuddies();
        this.getPendingBuddies();
        this.getRequests();
        this.trips = Trip.getTripsByUser(this);
    }

}
