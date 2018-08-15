package com.futuristlabs.p2p.emails;

import org.springframework.stereotype.Component;

@Component
public class MailNotifier {
    public void sendNewPasswordEmail(String email, String newPassword) {
        final String subject = "PriorityCoach Password Recovery";
        final String body = "Your new password is: <b>" + newPassword + "</b><br /><br />After you log into the system you can change it.<br /><br /><i>PriorityCoach team</i>";
        new MailSender(email, subject, body).send();
    }
}
