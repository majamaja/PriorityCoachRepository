package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.exceptions.NotModifiedException;
import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.func.pages.StaticPagesRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class JdbcStaticPagesRepository extends AbstractJdbcRepository implements StaticPagesRepository {
    @Override
    public StaticPage getPageByName(String pageName, LocalDateTime lastModifiedSince) {
        final String sql = "SELECT * FROM static_pages WHERE name = :name";
        final StaticPage page = db.getBean(StaticPage.class, sql, set("name", pageName));
        if (lastModifiedSince != null && page.getLastModified().isBefore(lastModifiedSince)) {
            throw new NotModifiedException();
        }
        return page;
    }
}
