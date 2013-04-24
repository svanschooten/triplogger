package models;

import org.joda.time.DateTime;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.*;
import play.data.validation.Constraints.*;

/**
 * Created with IntelliJ IDEA.
 * UserModel: Stijn
 * Date: 1-4-13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class Mail {

    public String subject;
    public String recipient;
    public String sender;
    public String text;
    public String HTML;

    public Mail(String subject, String recipient, String sender, String text, String HTML) {
        this.subject = subject;
        this.recipient = recipient;
        this.sender = sender;
        this.text = text;
        this.HTML = HTML;
    }


}
