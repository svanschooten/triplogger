package controllers;

import models.Drug;
import play.data.Form;
import play.mvc.*;
import models.Mail;
import com.typesafe.plugin.*;
import views.html.index;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Stijn
 * Date: 1-4-13
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Mailer extends Controller{

    public static Result mailTo() {
        Form<Mail> mailForm = Form.form(Mail.class).bindFromRequest();
        if (mailForm.hasErrors()) {
            return badRequest(index.render(new ArrayList<Drug>()));
        } else {
            Mail mail = new Mail(mailForm.get().subject,mailForm.get().recipient,mailForm.get().sender,mailForm.get().text,mailForm.get().HTML);
            return mailTo(mail.subject, mail.recipient, mail.sender, mail.text, mail.HTML);
        }
    }

    public static Result mailTo(String subject, String recipient, String sender, String text, String HTML) {
        MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
        mail.setSubject(subject);
        mail.addRecipient(recipient);
        mail.addFrom(sender);
        mail.send( text, HTML);
        return ok();
    }
}
