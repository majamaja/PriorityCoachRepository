package com.futuristlabs.utils.emails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    private final String smtpHost;
    private final Integer smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;
    private final String smtpFrom;
    private final String smtpFromAlias;
    private final boolean smtpUseSSL;

    private JavaMailSender mailSender;
    private final Log log = LogFactory.getLog(getClass());

    private JavaMailSenderImpl getMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(smtpUsername);
        mailSender.setPassword(smtpPassword);

        final Properties props = new Properties();
        props.setProperty("mail.smtp.from", smtpFrom);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", String.valueOf(smtpUseSSL));
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    public EmailSender(String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String smtpFrom, String smtpFromAlias, boolean smtpUseSSL) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.smtpFrom = smtpFrom;
        this.smtpFromAlias = smtpFromAlias;
        this.smtpUseSSL = smtpUseSSL;
        this.mailSender = getMailSender();
    }

    public boolean sendEmail(String toEmail, String subject, String content) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper mimeMessageHelper;
            mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setFrom(smtpFrom, smtpFromAlias);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
