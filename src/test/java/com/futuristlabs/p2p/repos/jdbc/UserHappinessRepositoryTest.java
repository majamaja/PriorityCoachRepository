package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.happiness.HappinessLevel;
import com.futuristlabs.p2p.func.happiness.UserHappinessRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class UserHappinessRepositoryTest extends RepositoryTest {

    @Autowired
    private UserHappinessRepository repo;

    @Test
    public void modifiedHappinessLevel() {
        final List<HappinessLevel> categories = repo.modifiedHappinessLevel(UUID.randomUUID(), new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedHappinessLevelNoDate() {
        final List<HappinessLevel> categories = repo.modifiedHappinessLevel(UUID.randomUUID(), null);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifyHappinessLevel() {
        final HappinessLevel happinessLevel = new HappinessLevel();
        happinessLevel.setId(UUID.randomUUID());
        happinessLevel.setLevel(8);
        happinessLevel.setCheckinDateAsDate(new LocalDate());

        final UUID user = sampleData.user();

        repo.modifyHappinessLevel(user, Arrays.asList(happinessLevel));
    }

    @Test
    public void modifyHappinessLevelNoData() {
        repo.modifyHappinessLevel(UUID.randomUUID(), new ArrayList<HappinessLevel>());
    }

    @Test
    public void modifyHappinessLevelNoDataNull() {
        repo.modifyHappinessLevel(UUID.randomUUID(), null);
    }

}
