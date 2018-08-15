package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.cfg.SystemParam;
import com.futuristlabs.p2p.func.cfg.SystemParametersRepository;
import com.futuristlabs.p2p.apn.ApnsNotificationProvider;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class SystemParametersTest extends RepositoryTest {

    @Autowired
    private SystemParametersRepository repo;

    @Test
    public void checkMethods() {
        // simply call each method to check for spelling mistakes.
        repo.getApnPassword();
        repo.getApnCertificate();
        repo.isApnProduction();
        repo.setParameter(new SystemParam("test", "test", null));
        repo.setParameter(new SystemParam("test", "test", new byte[] { 1, 2, 3}));
        repo.setParameter(new SystemParam("test", null, new byte[] { 1, 2, 3}));
    }

    @Ignore
    @Test
    public void sendPN() {
        final ApnsNotificationProvider apnsNotificationProvider = new ApnsNotificationProvider(repo);
        apnsNotificationProvider.notifyAll(Collections.singletonList("88efad94 2c06600e 036b14d1 41cc1373 07d28282 9fbb9d28 cf8dd1dc 8fc2b9a1"), "Hi buddy!");
    }
}
