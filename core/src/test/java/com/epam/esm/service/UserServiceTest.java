package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private UserService service;
    private UserDao dao;
    private List<User> users;
    private User user;
    private List<UserOrder> orders;
    private UserOrder order;

    @BeforeAll
    void beforeAll() {
        dao = mock(UserDao.class);
        service = new UserServiceImpl(dao);
    }

    @AfterAll
    void afterAll() {
        service = null;
        dao = null;
        users = null;
        user = null;
        orders = null;
        order = null;
    }

    @BeforeEach
    void setUpStubUser() {
        user = new User(0, "Stub");
    }

    @Test
    void createUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.create(user));
    }

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        user = new User(2, "Gleb");
        users.add(user);
    }

    @Test
    void findAllTest() {
        when(dao.findAll(1, User.class)).thenReturn(users);
        List<User> allUsers = service.findAll(1);
        Assertions.assertFalse(allUsers.isEmpty());
    }

    @BeforeEach
    void setUpUser() {
        user = new User();
    }

    @Test
    void findByIdTest() {
        when(dao.findById(1)).thenReturn(Optional.of(user));
        User actualUser = service.findById(1);
        Assertions.assertNotNull(actualUser);
    }

    @Test
    void deleteUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.delete(0));
    }

    @BeforeEach
    void setUpAllOrders() {
        orders = new ArrayList<>();
        UserOrder order = new UserOrder();
        orders.add(order);
    }

    @Test
    void findAllOrdersTest() {
        when(dao.findAllOrders(1,1)).thenReturn(orders);
        List<UserOrder> allOrders = service.findAllOrders(1, 1);
        Assertions.assertFalse(allOrders.isEmpty());
    }

    @BeforeEach
    void setUpOrder() {
        order = new UserOrder();
    }

    @Test
    void findOrderTest() {
        when(dao.findOrder(1,1)).thenReturn(Optional.of(order));
        UserOrder actualOrder = service.findOrder(1, 1);
        Assertions.assertNotNull(actualOrder);
    }
}
