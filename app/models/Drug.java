package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.String;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 30-3-13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class Drug extends Model{

    @Id
    public int did;


    public static Finder<Integer, Drug> find = new Finder(Integer.class, Drug.class);

    @Constraints.Required
    public String name;
    public String erowid;

    public Drug(String name, String erowid) {
        this.name = name;
        this.erowid = erowid;
    }

    public static void create(Drug drug) {
        drug.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<Drug> all(){
        return find.all();
    }
}
