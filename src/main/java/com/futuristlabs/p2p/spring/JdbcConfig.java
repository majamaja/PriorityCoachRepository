package com.futuristlabs.p2p.spring;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@EnableWebMvc
public class JdbcConfig {
    private static final String JDBC_DATABASE_URL = "JDBC_DATABASE_URL";
//    private static final String HEROKU_DATABASE_URL = "jdbc:mysql://eu-cdbr-west-02.cleardb.net/heroku_d738c6211c1565c?user=b1daec79145de9&password=ea770cf0";
    private static final String HEROKU_DATABASE_URL = "jdbc:mysql://localhost/p2p?user=p2p&password=p2p&useSSL=false";

    private DataSource dataSource;

    @PostConstruct
    public void init() {
        String databaseUrl = System.getenv(JDBC_DATABASE_URL);
        if (databaseUrl == null) {
            databaseUrl = HEROKU_DATABASE_URL;
        }

        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        this.dataSource = dataSource;
    }

    @Bean(destroyMethod = "close")
    public DataSource getDataSource() {
        return dataSource;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
