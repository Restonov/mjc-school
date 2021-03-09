package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void migrateFlyway() {
        flyway.migrate();
    }

    @Test
    void findByIdTest() {
        Assertions.assertTrue(dao.findById(15L).isPresent());
    }

    @Test
    void findMostProfitableUserTest() {
        User user = dao.findUsersBySumOfOrdersDesc().get(0);
        Assertions.assertEquals(15, user.getId());
    }

    @Test
    void saveUserTest() {
        User user = new User("Gleb");
        user.setPassword("123456");
        final User savedUser = dao.save(user);
        Assertions.assertEquals("Gleb", savedUser.getName());
    }
}
