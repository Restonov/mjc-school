package com.epam.esm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Persistence configuration
 */
@Configuration
@Profile("dev")
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.epam.esm.dao")
@PropertySource({"classpath:persistence.properties"})
public class PersistenceConfiguration {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUserName;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriver;
    @Value("${spring.datasource.cache-prep-stmts}")
    private String cachePrepStmts;
    @Value("${spring.datasource.prep-stmt-cache-size}")
    private String cacheSizePrepStmts;
    @Value("${spring.datasource.prep-stmt-cache-limit}")
    private String cacheLimitPrepStmts;

    /**
     * Exception i18n
     *
     * @return Message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor postProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * That need for ${some.property} work in @Value marked variable
     *
     * @return PropertySourcesPlaceholderConfigurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfiguration() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Manual configuration for Json patch
     *
     * @return ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper geObjMapper() {
        return new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    /**
     * Data source ~ DB
     *
     * @return DataSource
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUserName);
        config.setPassword(jdbcPassword);
        config.setDriverClassName(jdbcDriver);
        config.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        config.addDataSourceProperty("prepStmtCacheSize", cacheSizePrepStmts);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", cacheLimitPrepStmts);
        return new HikariDataSource(config);
    }
}