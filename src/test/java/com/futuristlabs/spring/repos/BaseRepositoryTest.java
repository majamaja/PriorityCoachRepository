package com.futuristlabs.spring.repos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryTestConfig.class})
@Transactional(isolation = REPEATABLE_READ)
public class BaseRepositoryTest {

    @Autowired
    protected SampleData sampleData;

    @Test
    public void jdbcLoads() {
    }
}
