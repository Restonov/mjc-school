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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Test config
 */

@Log4j2
@Profile("qa")
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
@PropertySource({"classpath:test_persistence.properties"})
public class JUnitConfig implements WebMvcConfigurer {
    private static final String PACKAGES_TO_SCAN = "com.epam.esm.entity";

    private final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager =
                new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan(PACKAGES_TO_SCAN);
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(adapter);
        entityManager.setJpaProperties(hibernateProperties());
        return entityManager;
    }

    @Bean
    @Autowired
    public CriteriaBuilder criteriaBuilder(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getNativeEntityManagerFactory().getCriteriaBuilder();
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(final EntityManagerFactory factory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);
        return transactionManager;
    }

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

    private Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProp.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        return hibernateProp;
    }
}
