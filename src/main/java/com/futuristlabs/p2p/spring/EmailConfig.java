package com.futuristlabs.p2p.spring;

import com.futuristlabs.func.properties.SystemPropertiesRepository;
import com.futuristlabs.utils.emails.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.futuristlabs.func.properties.SystemProperty.*;

@Configuration
public class EmailConfig {

    @Autowired
    private SystemPropertiesRepository properties;

    @Bean
    public EmailSender emailSender() {
//        final String host = properties.findByName(SMTP_HOST).get();
//        final int port = Integer.parseInt(properties.findByName(SMTP_PORT).get());
//        final String user = properties.findByName(SMTP_USERNAME).get();
//        final String pass = properties.findByName(SMTP_PASSWORD).get();
//        final String from = properties.findByName(SMTP_FROM).get();
//        final String alias = properties.findByName(SMTP_FROM_ALIAS).get();
//        final boolean useSSL = Boolean.parseBoolean(properties.findByName(SMTP_USE_SSL).get());

//        return new EmailSender(host, port, user, pass, from, alias, useSSL);
        // TODO XXX
        return null;
    }
}
