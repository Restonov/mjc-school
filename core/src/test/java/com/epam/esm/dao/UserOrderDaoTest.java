package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.builder.UserOrderBuilder;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserOrderDaoTest {
    private User user;
    private GiftCertificate certificate;
    private UserOrder order;
    private Pageable pageWithResult;

    @Autowired
    private UserOrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GiftCertificateDao certDao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void migrateFlyway() {
        flyway.migrate();
    }

    @AfterAll
    public void tearDown() {
        user = null;
        certificate = null;
        order = null;
        pageWithResult = null;
    }

    @BeforeEach
    void setUpOrder() {
        user = userDao.findById(10L).orElseThrow(ResourceNotFoundException::new);
        certificate = certDao.findById(4L).orElseThrow(ResourceNotFoundException::new);
        order = UserOrderBuilder.build(user, certificate);
    }

    @Test
    void addOrderTest() {
        UserOrder userOrder = orderDao.save(order);
        Assertions.assertNotNull(userOrder);
    }

    @BeforeEach
    void setUpFindAllOrdersTest() {
        pageWithResult = PageRequest.of(0, 5);

    }

    @Test
    void findAllOrdersTest() {
        final Optional<User> optionalUser = userDao.findById(15L);
        final User user = optionalUser.orElseThrow(ResourceNotFoundException::new);
        Page<UserOrder> allOrders = orderDao.findAllByUser(user, pageWithResult);
        Assertions.assertFalse(allOrders.isEmpty());
    }

    @Test
    void findOrderTest() {
        final Optional<User> optionalUser = userDao.findById(20L);
        final User user = optionalUser.orElseThrow(ResourceNotFoundException::new);
        Optional<UserOrder> order = orderDao.findByIdAndUser(156, user);
        Assertions.assertTrue(order.isPresent());
    }
}