package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * Main app config
 */
@ComponentScan("com.epam")
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("classpath:db_configuration.properties")
@RequiredArgsConstructor
public class ApplicationConfig implements WebMvcConfigurer {

    private final Environment env;

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl( env.getProperty("jdbc.url"));
        config.setUsername( env.getProperty("db.username"));
        config.setPassword( env.getProperty("db.password"));
        config.setDriverClassName( env.getProperty("driver.classname"));
        config.addDataSourceProperty( "cachePrepStmts", env.getProperty("cache.prep.stmts"));
        config.addDataSourceProperty( "prepStmtCacheSize", env.getProperty("prep.stmt.cache.size"));
        config.addDataSourceProperty( "prepStmtCacheSqlLimit", env.getProperty("prep.stmt.cache.limit"));
        return new HikariDataSource( config );
    }
}
