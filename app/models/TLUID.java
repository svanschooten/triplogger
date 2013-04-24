package models;

import play.db.ebean.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 24-4-13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class TLUID extends Model{

    @Id
    public int tluid;

    public static Model.Finder<Integer, TLUID> find = new Model.Finder(Integer.class, TLUID.class);

    private static void makeID(int id) {
        TLUID tl = new TLUID();
        tl.tluid = id;
        tl.save();
    }

    public static int create() {

        Integer uid = -1;
        TLUID tmp = new TLUID();
        while( tmp != null) {
            uid = UUID.randomUUID().hashCode();
            tmp = find(uid);
        }
        makeID(uid);
        return uid;
    }

    private static TLUID find(int t){
        return find.where().eq("tluid", t).findUnique();
    }
}
