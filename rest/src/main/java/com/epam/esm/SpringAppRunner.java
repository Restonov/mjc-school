package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.epam")
public class SpringAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringAppRunner.class, args);
    }

    /** TODO + Spring Security. Hibernate > Spring Data
     *
     * 1. Spring security activation
     * 2. Migrate to Spring Data
     * 3. Auth via JWT (Roles, DB storing)
     * 4. (optional) OAUTH2 + OpenID
     *
     */

}
