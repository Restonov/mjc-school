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

     // TODO list Spring > Spring Boot
     // 1. Change single field of gift certificate
     // 2. Add new entity User. Implement only get operations
     // 3. Make an order on gift certificate for a user
     // 4. Get information about user’s order / orders : cost and timestamp of a purchase
     // 5. Get the most widely used tag of a user with the highest cost of all orders
     // 6. Search for gift certificates by several tags
     // 7. Pagination should be implemented for all GET endpoints
     // 8. Support HATEOAS on REST endpoints
     // 9. i18n
     // 10. audit
}
