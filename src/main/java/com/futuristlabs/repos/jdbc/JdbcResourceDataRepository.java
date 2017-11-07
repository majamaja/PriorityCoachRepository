package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.resources.ResourceData;
import com.futuristlabs.func.resources.ResourceDataRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcResourceDataRepository extends AbstractJdbcRepository implements ResourceDataRepository {
    @Override
    public UUID create(ResourceData resourceData) {
        final String sql = "INSERT INTO resources(mime_type, content) VALUES (:mimeType, :content)";
        return db.insert(sql, resourceData).getId();
    }

    @Override
    public ResourceData getById(UUID id) {
        final String sql = "SELECT * FROM resources WHERE id = :id";
        return db.getBean(ResourceData.class, sql, set("id", id));
    }
}
