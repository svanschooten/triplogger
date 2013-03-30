package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.String;
import java.util.List;

import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 30-3-13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class Measure extends Model{

    @Id
    public int mid;

    @Constraints.Required
    public String name;
    public String display;

    public static Finder<Integer, Measure> find = new Finder(Integer.class, Measure.class);

    public static void create(Measure measure) {
        measure.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<Measure> all(){
        return find.all();
    }
}
