package models;


import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.String;

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

    public String name;
    public String erowid;
}
