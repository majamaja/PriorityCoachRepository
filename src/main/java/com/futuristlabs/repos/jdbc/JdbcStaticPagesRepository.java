package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.func.pages.StaticPagesRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcStaticPagesRepository extends AbstractJdbcRepository implements StaticPagesRepository {

    @Override
    public List<StaticPage> findAll() {
        final String sql = "SELECT * FROM static_pages ORDER BY name";
        return db.list(StaticPage.class, sql);
    }

    @Override
    public StaticPage findById(UUID pageId) {
        final String sql = "SELECT * FROM static_pages WHERE id = :id";
        return db.getBean(StaticPage.class, sql, set("id", pageId));
    }

    @Override
    public StaticPage findByName(String pageName) {
        final String sql = "SELECT * FROM static_pages WHERE name = :name";
        return db.getBean(StaticPage.class, sql, set("name", pageName));
    }

    @Override
    public UUID insert(StaticPage page) {
        final String sql = "INSERT INTO static_pages (name, header, content) VALUES (:name, :header, :content)";
        return db.insert(sql, page).getId();
    }

    @Override
    public void update(StaticPage page) {
        final String sql =
                " UPDATE static_pages " +
                " SET name = :name, header = :header, content = :content, updated_at = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                " WHERE id = :id";
        db.update(sql, page);
    }

    @Override
    public void deleteById(UUID pageId) {
        db.update("DELETE FROM static_pages WHERE id = :id", set("id", pageId));
    }
}
