package com.epam.esm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;

/**
 * Test config
 */

@Log4j2
@Profile("qa")
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EntityScan(basePackages = "com.epam.esm")
@EnableJpaRepositories(basePackages = "com.epam.esm.dao")
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class JUnitConfig {

    @Bean
    public PersistenceExceptionTranslationPostProcessor postProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    @Primary
    public ObjectMapper geObjMapper(){
        return new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Bean(initMethod = "migrate")
    @Primary
    public Flyway flyway() {
        FluentConfiguration configuration = Flyway.configure()
                .dataSource(dataSource())
                .locations("db.migration")
                .baselineOnMigrate(true);
        return configuration.load();
    }

    @Bean
    public DataSource dataSource() {
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
