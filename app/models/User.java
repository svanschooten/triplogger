package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 30-3-13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class User extends Model{

    @Id
    public int uid;

    public String alias;
    public String email;
    public int trippoints;
    public String password;
    public ArrayList<Trip> trips;

}
