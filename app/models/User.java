package models;

import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    public int uid;
    public String alias;

    @Id
    public String email;

    public int trippoints;
    public String password;
    public ArrayList<Trip> trips;
    public boolean validated;

    public User(String alias, String email, String password) {
        this.alias = alias;
        this.email = email;
        this.password = Crypto.encryptAES(password);
        trippoints = 0;
        trips = new ArrayList<>();
        validated = false;
    }

    public static void init() {
        if(authenticate("svsoke", "password")== null) {
            User user = new User("svsoke", "svsoke@hotmail.com", "password");
            user.validated = true;
            create(user);
        }
        if(authenticate("mcawesome", "password")== null) {
            User user = new User("mcawesome", "elgar.groot@gmail.com", "password");
            user.validated = true;
            create(user);
        }
    }

    public void addTrip(Trip trip){
        trip.addBuddy(this);
        trips.add(trip);
        create(this);
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



}
