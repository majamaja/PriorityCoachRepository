package com.futuristlabs.repos;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

@Configuration
@ComponentScan({ "com.futuristlabs.repos.jdbc" })
@EnableTransactionManagement(proxyTargetClass = true)
public class RepositoryTestConfig {
    private static final String JDBC_DATABASE_URL = "JDBC_DATABASE_URL";
    private static final String HEROKU_DATABASE_URL = "jdbc:postgresql://ec2-54-235-150-134.compute-1.amazonaws.com:5432/d3eq15mr8bfpj2?user=zmrbkuocinmqrx&password=2b5cc4bb69807f86fe83476b715d5457ead48e10162209e68ae17dab9d80dfcb&sslmode=require";
    private static final String JDBC_URL = HEROKU_DATABASE_URL;

    private DataSource dataSource;

    @PostConstruct
    public void init() {
        String databaseUrl = System.getenv(JDBC_DATABASE_URL);
        if (databaseUrl == null) {
            databaseUrl = JDBC_URL;
        }

        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        dataSource.setDefaultTransactionIsolation(TRANSACTION_REPEATABLE_READ);
        this.dataSource = dataSource;
    }

    @Bean(destroyMethod = "close")
    public DataSource getDataSource() {
        return dataSource;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(dataSource) {
            @Override
            protected void doCommit(DefaultTransactionStatus status) {
                doRollback(status);
            }
        };
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public SampleData getSampleData() {
        return new SampleData();
    }
}
