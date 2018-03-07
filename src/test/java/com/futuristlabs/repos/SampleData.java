package com.futuristlabs.repos;


import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import com.futuristlabs.repos.jdbc.common.Parameters;

import java.util.UUID;
import java.util.function.BiConsumer;

public class SampleData extends AbstractJdbcRepository {

    // Static pages
    public StaticPage staticPage() {
        final UUID id = UUID.randomUUID();
        final String name = "page-name." + id.toString();
        final String header = "page-header." + id.toString();
        final String content = "page-content." + id.toString();
        return staticPage(id, name, header, content);
    }

    public StaticPage staticPage(final UUID id, final String name, final String header, final String content) {
        return insert(StaticPage.class, "static_pages (id, name, header, content)", id, name, header, content);
    }

    // Helpers
    public void list(String table) {
        System.out.println("Listing:____________________" + table);

        final String sql = String.format(" SELECT * FROM %s ", table);
        db.list((rs, idx) -> {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                System.out.print("  " + rs.getMetaData().getColumnName(i) + ": " + rs.getObject(i) + "\t");
            }
            System.out.println();
            return null;
        }, sql);

        System.out.println("END\n");
    }

    private <T> T insert(final Class<T> beanClass, final String table, final Object value0, final Object... values) {
        final Parameters params = new Parameters();
        final StringBuilder sql = new StringBuilder(" INSERT INTO ");

        final BiConsumer<Integer, Object> insertBuilder = (index, value) -> {
            final String sindex = String.valueOf(index);
            sql.append(':').append(sindex);
            params.set(sindex, value);
        };

        sql.append(table).append(" VALUES ( ");
        insertBuilder.accept(0, value0);

        int index = 1;
        for (Object value : values) {
            sql.append(", ");
            insertBuilder.accept(index, value);
            index++;
        }
        sql.append(" ) RETURNING * ");

        return db.getBean(beanClass, sql.toString(), params);
    }
}
