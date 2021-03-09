package com.epam.esm.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;

/**
 * Test config
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm.dao.impl")
@Profile("qa")
public class JUnitConfig implements WebMvcConfigurer {

    @Bean
    public DataSourceTransactionManager getTransactionManager(){
        return new DataSourceTransactionManager(getEmbeddedDataSource());
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getEmbeddedDataSource());
    }

    @Bean
    public Flyway flyway() {
        FluentConfiguration configuration = Flyway.configure()
                .dataSource(getEmbeddedDataSource())
                .locations("db.migration")
                .baselineOnMigrate(true);
        return configuration.load();
    }

    @Bean
    public DataSource getEmbeddedDataSource() {
        EmbeddedPostgres embeddedPostgres = null;
        try {
            embeddedPostgres = EmbeddedPostgres.builder().start();
        } catch (IOException e) {
            log.error("Embedded db creation error", e);
        }
        return Objects.requireNonNull(embeddedPostgres,
                "EmbeddedPostgres can't be null").getPostgresDatabase();
    }
}
