package com.epam.esm.config;

import com.epam.esm.filter.ExceptionHandlingFilter;
import com.epam.esm.filter.JwtAuthenticationFilter;
import com.epam.esm.handler.CustomAccessDeniedHandler;
import com.epam.esm.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

import static com.epam.esm.entity.Constants.ADMINISTRATOR;
import static com.epam.esm.entity.Constants.AUTH_GENERATE_TOKEN_URL;
import static com.epam.esm.entity.Constants.AUTH_SIGNUP_URL;
import static com.epam.esm.entity.Constants.SHUTDOWN_URL;
import static com.epam.esm.entity.Constants.CERTIFICATE_BY_ID_URL;
import static com.epam.esm.entity.Constants.CREATE_USER_ORDER_URL;
import static com.epam.esm.entity.Constants.MOST_PROFIT_TAG_URL;
import static com.epam.esm.entity.Constants.USER;

/**
 * Web security configuration
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ExceptionHandlingFilter exceptionHandlerFilterBean() {
        return new ExceptionHandlingFilter();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilterBean() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPointBean() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandlerBean() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, AUTH_SIGNUP_URL).anonymous()
                .antMatchers(AUTH_GENERATE_TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.GET, SHUTDOWN_URL).permitAll()
                .antMatchers(HttpMethod.GET, CERTIFICATE_BY_ID_URL).permitAll()
                .antMatchers(HttpMethod.GET, MOST_PROFIT_TAG_URL).hasRole(ADMINISTRATOR)
                .antMatchers(HttpMethod.GET).hasAnyRole(USER, ADMINISTRATOR)
                .antMatchers(HttpMethod.POST, CREATE_USER_ORDER_URL).hasAnyRole(USER, ADMINISTRATOR)
                .anyRequest().hasRole(ADMINISTRATOR)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(exceptionHandlerFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointBean())
                .accessDeniedHandler(accessDeniedHandlerBean());
    }
}
