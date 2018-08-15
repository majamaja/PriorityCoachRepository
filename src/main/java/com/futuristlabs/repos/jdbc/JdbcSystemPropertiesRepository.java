package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.properties.SystemPropertiesRepository;
import com.futuristlabs.func.properties.SystemProperty;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import com.futuristlabs.repos.jdbc.common.NewSpringDB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcSystemPropertiesRepository extends AbstractJdbcRepository implements SystemPropertiesRepository {
    private static final Log log = LogFactory.getLog(JdbcSystemPropertiesRepository.class);
    private Map<String, Optional<String>> properties;

    @Autowired
    @Override
    public void setDb(final NewSpringDB db) {
        super.setDb(db);

        // initialize properties - this SHOULD be thread safe as the setter SHOULD be called only once by Spring.
        final String sql = "SELECT parameter_key as name, parameter_value as value FROM system_parameters";
        this.properties = db.map(getString("name"), getString("value").optional(), sql);

        if (log.isInfoEnabled()) {
            log.info("System Properties:");
            properties.forEach((name, value) -> {
                log.info(String.format("%s=%s", name, value));
            });
        }
    }

    @Override
    public Optional<String> findByName(SystemProperty property) {
        return properties.get(property.name());
    }
}
