package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.libs.Crypto;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 1-4-13
 * Time: 12:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class ValidateRequest  extends Model{


    @GeneratedValue
    @Id
    public int vrid;

    public User target;
    public int targetId;
    public String token;

    public static Model.Finder<Integer, ValidateRequest> find = new Model.Finder(Integer.class, ValidateRequest.class);

    public ValidateRequest(User target) {
        this.target = target;
        this.targetId = target.uid;
        Random rand = new Random();
        token = Crypto.encryptAES("" + rand.nextInt()) +
                Crypto.encryptAES(target.alias) +
                Crypto.encryptAES("" + rand.nextInt());
    }

    public static void create(ValidateRequest request) {
        request.save();
    }

    public static void delete(int id) {
        find.ref(id).delete();
    }

    public static List<ValidateRequest> all(){
        return find.all();
    }

    public static ValidateRequest findRequest(String token) {
        return find.where().eq("token", token).findUnique();
    }

    public void validate() {
        target = User.findById(targetId);
        target.validated = true;
        target.save();
        this.delete();
    }
}
