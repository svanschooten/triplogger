package models;


import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import play.data.format.Formats;
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

    @Id
    public Integer tid;

    public static Finder<Integer, Trip> find = new Finder(Integer.class, Trip.class);

    public ArrayList<User> withBuddy;
    public String comments;

    @Required
    public Drug drug;
    public Formats.DateTime from;
    public Formats.DateTime till;
    public int number;
    public Measure measure;

    public Trip(Drug drug, Formats.DateTime from, Formats.DateTime till, int number, Measure measure, String comments) {
        this.drug = drug;
        this.from = from;
        this.till = till;
        this.number = number;
        this.measure = measure;
        this.comments = comments;
        withBuddy = new ArrayList<>();
    }

    public void addBuddy(User user) {
        if(!withBuddy.contains(user)){
            withBuddy.add(user);
        }
    }

    public static void create(Trip trip) {
        trip.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<Trip> all(){
        return find.all();
    }
}
