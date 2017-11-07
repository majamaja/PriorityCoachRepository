package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.properties.SystemPropertiesRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import com.futuristlabs.repos.jdbc.common.SpringDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcSystemPropertiesRepository extends AbstractJdbcRepository implements SystemPropertiesRepository {
    private Map<String, Optional<String>> properties;

    @Autowired
    @Override
    public void setDb(final SpringDB db) {
        super.setDb(db);

        // initialize properties - this SHOULD be thread safe as the setter SHOULD be called only once by Spring.
        final String sql = "SELECT name, value FROM system_properties";
        this.properties = db.map(getString("name"), getString("value").optional(), sql);
    }

    @Override
    public Optional<String> findByName(String name) {
        return properties.get(name);
    }
}
