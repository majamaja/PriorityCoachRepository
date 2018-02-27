package com.futuristlabs.func.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StaticPages {
    private final StaticPagesRepository repository;

    @Autowired
    public StaticPages(StaticPagesRepository repository) {
        this.repository = repository;
    }

    public List<StaticPage> readAll() {
        return repository.findAll();
    }

    public StaticPage readById(final UUID pageId) {
        return repository.findById(pageId);
    }

    public StaticPage readByName(final String name) {
        return repository.findByName(name);
    }

    @Transactional
    public StaticPage create(final StaticPage page) {
        final UUID pageId = repository.insert(page);
        page.setId(pageId);
        return page;
    }

    @Transactional
    public StaticPage update(final StaticPage page) {
        repository.update(page);
        return page;
    }

    @Transactional
    public void deleteById(final UUID pageId) {
        repository.deleteById(pageId);
    }
}
