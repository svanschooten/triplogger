package models;


import play.data.format.Formats;
import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

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
    public int tid;

    public Drug drug;
    public Formats.DateTime from;
    public Formats.DateTime till;
    public int number;
    public Measure measure;
    public ArrayList<User> withBuddy;
    public String comments;

}
