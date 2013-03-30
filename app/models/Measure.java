package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.String;
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
    public String name;
    public String display;
}
