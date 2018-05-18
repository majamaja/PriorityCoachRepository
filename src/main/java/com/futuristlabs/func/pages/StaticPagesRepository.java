package com.futuristlabs.func.pages;

import java.util.List;
import java.util.UUID;

public interface StaticPagesRepository {
    List<StaticPage> findAll();

    StaticPage findById(UUID pageId);

    StaticPage findByName(String pageName);

    UUID insert(StaticPage page);

    void update(StaticPage page);

    void deleteById(UUID pageId);
}
