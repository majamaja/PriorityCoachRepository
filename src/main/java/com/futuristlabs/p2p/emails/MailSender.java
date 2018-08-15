package com.futuristlabs.p2p.emails;

//import com.google.appengine.api.utils.SystemProperty;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

public class MailSender {

    private static final Logger log = Logger.getLogger(MailSender.class.getName());

    private String recipient;
    private String subject;
    private String msgBody;
    private String senderAddress;
    private String sender = "PriorityCoach";
    private String senderPass = "";

    public MailSender(String toEmail, String subject, String messageBody) {
        this.recipient = toEmail;
        this.subject = subject;
        this.msgBody = messageBody;
//        this.senderAddress = String.format("noreply@%s.appspotmail.com", SystemProperty.applicationId.get());
        this.senderAddress = String.format("noreply@%s.appspotmail.com", "p2p");
    }

    public void send() {
        final Properties props = new Properties();
        final Authenticator auth = new SMTPAuthenticator();
        final Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderAddress, sender));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(msgBody, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (Exception e) {
            log.severe("Cannot send mail: " + e.getCause());
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            final String username = senderAddress;
            final String password = senderPass;

            return new PasswordAuthentication(username, password);
        }
    }
}
