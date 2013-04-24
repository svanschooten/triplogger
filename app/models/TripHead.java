package models;


import play.data.format.Formats;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.data.validation.Constraints.*;


/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 30-3-13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class TripHead extends Model{

    @GeneratedValue
    private Integer id;

    @Id
    public int tid;

    public static Finder<Integer, TripHead> find = new Finder(Integer.class, TripHead.class);

    @Required
    public Drug drug;
    public int drugId;

    @Formats.DateTime(pattern="dd/MM/yyyy-hh:mm:ss")
    public Date dfrom;

    @Formats.DateTime(pattern="dd/MM/yyyy-hh:mm:ss")
    public Date dtill;


    public TripHead(Drug drug, Date from, Date till) {
        this.drug = drug;
        this.drugId = drug.did;
        this.dfrom = from;
        this.dtill = till;
    }

    public TripHead(Drug drug) {
        this.drug = drug;
        this.dfrom = new Date();
        this.dtill = new Date();
    }

    public void create() {
        this.tid = TLUID.create();
        this.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<TripHead> all(){
        return find.all();
    }

    public static TripHead findById(int id){
        return find.where().eq("tid", id).findUnique();
    }

    public static List<Drug> findDrugsUsed(UserModel u){
        if(u == null) {
            return new ArrayList<Drug>();
        }
        ArrayList<Drug> result = new ArrayList<>();
        List<TripLink> links = TripLink.findTrips(u);
        for(TripLink link : links) {
            TripHead t = TripHead.findById(link.tripId);
            result.add(Drug.findById(t.drugId));
        }
        return result;
    }


    public static List<TripHead> getByUser(UserModel u) {
        List<TripLink> links = TripLink.findTrips(u);
        List<TripHead> result = new ArrayList<>();
        for(TripLink link : links) {
            TripHead t = TripHead.findById(link.tripId);
            result.add(t);
        }
        return result;
    }
}
