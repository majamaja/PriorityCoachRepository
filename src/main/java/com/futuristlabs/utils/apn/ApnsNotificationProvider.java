package com.futuristlabs.utils.apn;

import com.futuristlabs.func.pn.NotificationProvider;
import com.futuristlabs.func.users.devices.UserDevice;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApnsNotificationProvider implements NotificationProvider {
    final String APN_CERT_PASSWORD = "";

    @Value(value = "classpath:APNCertificatesDev.p12")
    private Resource apnDevCertificate;

    @Value(value = "classpath:APNCertificatesProd.p12")
    private Resource apnProdCertificate;

    @Override
    public void notifyAll(String payload, List<UserDevice> devices, boolean isDev) {
        ApnsService service = null;
        try {
            final boolean isProduction = !isDev;
            final Resource apnCertificate = isDev ? apnDevCertificate : apnProdCertificate;

            service = APNS.newService()
                    .withCert(apnCertificate.getInputStream(), APN_CERT_PASSWORD)
                    .withAppleDestination(isProduction)
                    .build();

            final List<String> tokens = devices.stream()
                    .filter(device -> device.getType().equals("iOS"))
                    .map(UserDevice::getToken)
                    .collect(Collectors.toList());

            System.out.println(String.format("service: %s, is prod: %b", service, isProduction));
            System.out.println(payload);
            for (String tok : tokens) {
                System.out.println(tok);
            }

            service.testConnection();

            final Collection<? extends ApnsNotification> push = service.push(tokens, payload);
            for (ApnsNotification apnsNotification : push) {
                System.out.println(apnsNotification.toString());
            }

//            Map<String, Date> inactiveDevices = service.getInactiveDevices();
//            for (Map.Entry<String, Date> dev : inactiveDevices.entrySet()) {
//                System.out.println(String.format("Device %s inactive as of: %s", dev.getKey(), dev.getValue()));
//            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            if (service != null) {
                service.stop();
            }
        }
    }
}
