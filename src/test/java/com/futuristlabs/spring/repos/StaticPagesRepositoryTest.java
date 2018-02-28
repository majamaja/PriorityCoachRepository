package com.futuristlabs.spring.repos;


import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.func.pages.StaticPagesRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

import static org.junit.Assert.*;

public class StaticPagesRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private StaticPagesRepository staticPages;

    @Test(expected = EmptyResultDataAccessException.class)
    public void findById_MissingPage() {
        staticPages.findById(UUID.randomUUID());
    }

    @Test
    public void findById_ExistingPage() {
        final StaticPage page = sampleData.staticPage();
        final StaticPage found = staticPages.findById(page.getId());
        assertEquals(page, found);
    }
}
