package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import play.libs.Crypto;


/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 30-3-13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class User extends Model{

    public static Finder<Integer, User> find = new Finder(Integer.class, User.class);

    @Id
    @GeneratedValue
    public int uid;
    public String alias;
    public String email;

    public int trippoints;
    public String password;


    public ArrayList<Trip> trips = new ArrayList<>();
    public ArrayList<User> buddies = new ArrayList<>();
    public ArrayList<User> pendingBuddies = new ArrayList<>();
    public ArrayList<User> requests = new ArrayList<>();
    public boolean validated;

    public User(String alias, String email, String password) {
        this.alias = alias;
        this.email = email;
        this.password = Crypto.encryptAES(password);
        trippoints = 0;
        validated = false;
    }

    public static void init() {
        if(authenticate("svsoke", "password")== null && authenticate("mcawesome", "password")== null) {
            User stijn = new User("svsoke", "svsoke@hotmail.com", "password");
            User elgar = new User("mcawesome", "elgar.groot@gmail.com", "password");
            stijn.validated = true;
            elgar.validated = true;
            create(stijn);
            create(elgar);
            BuddyLink bl = new BuddyLink(stijn, elgar);
            bl.setValidated();
            BuddyLink.create(bl);

        }
    }

    public void setBuddies(ArrayList<User> buddyList) {
        this.buddies = buddyList;
    }

    public void setPendingBuddies(ArrayList<User> buddyList) {
        this.pendingBuddies = buddyList;
    }

    public static User findById(int id){
        return find.where().eq("uid", id).findUnique();
    }

    public static User findByAlias(String alias){
        return find.where().eq("alias", alias).findUnique();
    }

    public static List<User> findLikeAlias(String alias){
        return find.where().ilike("alias", "%" + alias + "%").findList();
    }

    public static ArrayList<User> getBuddiesList(User u) {
        List<BuddyLink> budds = BuddyLink.findBuddies(u);
        ArrayList<User> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(User.findById(bl.buddyId));
        }
        return buddies;
    }

    public static ArrayList<User> getPendingBuddiesList(User u) {
        List<BuddyLink> budds = BuddyLink.findPendingBuddies(u);
        ArrayList<User> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(User.findById(bl.buddyId));
        }
        return buddies;
    }

    public static ArrayList<User> getRequestsList(User u) {
        List<BuddyLink> budds = BuddyLink.getRequests(u);
        ArrayList<User> buddies = new ArrayList<>();
        for(BuddyLink bl : budds){
            buddies.add(User.findById(bl.userId));
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

    public void addTrip(Trip trip){
        if(!trips.contains(trip.tid)) {
            trips.add(trip);
            create(this);
        }
    }

    public void addBuddy(User u) {
        if(BuddyLink.exists(this, u) == null) {
            BuddyLink.create(new BuddyLink(this, u));
        }
    }

    public void addPoints(int p) {
        this.trippoints += p;
        create(this);
    }

    public static void create(User user) {
        user.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<User> all(){
        return find.all();
    }

    public static User authenticate(String alias, String password) {
        return find.where().eq("alias", alias).eq("password", Crypto.encryptAES(password)).eq("validated", true).findUnique();
    }

    public void restore() {
        this.getBuddies();
        this.getPendingBuddies();
        this.getRequests();
    }

}
