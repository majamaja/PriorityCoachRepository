package com.futuristlabs.p2p.apn;

import com.futuristlabs.p2p.func.cfg.SystemParametersRepository;
import com.futuristlabs.p2p.func.chat.NotificationProvider;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

@Component
public class ApnsNotificationProvider implements NotificationProvider {
    private SystemParametersRepository params;

    @Autowired
    public ApnsNotificationProvider(SystemParametersRepository params) {
        this.params = params;
    }

    @Override
    public void notifyAll(Collection<String> tokens, String message) {
        final byte[] apnCertificate = params.getApnCertificate();
        if (apnCertificate == null) {
            return;
        }

        final InputStream cert = new ByteArrayInputStream(apnCertificate);

        final ApnsService service = APNS.newService()
                .withCert(cert, params.getApnPassword())
                .withAppleDestination(params.isApnProduction())
                .build();

        final String msg = APNS.newPayload()
                               .alertBody(message)
                               .badge(1)
                               .customField("operation", "new_message")
                               .build();

        final Collection<? extends ApnsNotification> push = service.push(tokens, msg);
        for (ApnsNotification apnsNotification : push) {
            System.out.println(apnsNotification.toString());
        }
    }
}
