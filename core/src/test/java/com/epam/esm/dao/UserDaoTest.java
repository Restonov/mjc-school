package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Assertions.assertTrue(dao.findById(15).isPresent());
    }

    @Test
    void findMostProfitableUserTest() {
        User user = dao.findMostProfitableUser();
        Assertions.assertEquals(15, user.getId());
    }

    @Test
    void findAllOrdersTest() {
        List<UserOrder> allOrders = dao.findAllOrders(1, 10);
        Assertions.assertFalse(allOrders.isEmpty());
    }

    @Test
    void findOrderTest() {
        Optional<UserOrder> order = dao.findOrder(20, 156);
        Assertions.assertTrue(order.isPresent());
    }

    @Test
    void addUnsupportedTest() {
        User user = new User();
        Assertions.assertThrows(UnsupportedOperationException.class,
                    () -> dao.add(user));
    }

    @Test
    void deleteUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> dao.delete(0));
    }
}
