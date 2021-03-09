package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.impl.UserOrderServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserOrderServiceTest {
    private UserOrderService service;
    private UserOrderDao orderDao;
    private UserDao userDao;
    private GiftCertificateDao certificateDao;
    private UserOrder order;
    private User user;
    private GiftCertificate certificate;
    private Pageable pageAndResultPerPage;
    private Page<UserOrder> orderPage;

    @BeforeAll
    void beforeAll() {
        orderDao = mock(UserOrderDao.class);
        userDao = mock(UserDao.class);
        certificateDao = mock(GiftCertificateDao.class);
        service = new UserOrderServiceImpl(orderDao, userDao, certificateDao);
    }

    @AfterAll
    void afterAll() {
        service = null;
        orderDao = null;
        userDao = null;
        certificateDao = null;
        order = null;
        user = null;
        certificate = null;
        pageAndResultPerPage = null;
        orderPage = null;
    }

    @BeforeEach
    void setUpCreateTest() {
        user = new User(1);
        certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setPrice(BigDecimal.valueOf(100));
        order = new UserOrder();
        order.setUser(user);
        order.setCertificate(certificate);
    }

    @Test
    void createTest() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(certificateDao.findById(1L)).thenReturn(Optional.of(certificate));
        when(orderDao.save(order)).thenReturn(order);

        final UserOrder userOrder = service.create(order);
        verify(userDao).findById(1L);
        verify(certificateDao).findById(1L);
        verify(orderDao).save(order);
        Assertions.assertNotNull(userOrder);
    }

    @BeforeEach
    void setUpFindUserOrdersTest() {
        user = new User(2);
        pageAndResultPerPage = PageRequest.of(0, 10);
        orderPage = new PageImpl<>(new ArrayList<>());
    }

    @Test
    void findUserOrdersTest() {
        when(userDao.findById(2L)).thenReturn(Optional.of(user));
        when(orderDao.findAllByUser(user, pageAndResultPerPage)).thenReturn(orderPage);

        final Page<UserOrder> userOrders = service.findUserOrders(0, 10, 2);
        Assertions.assertNotNull(userOrders);
    }

    @BeforeEach
    void setUpFindUserOrderTest() {
        user = new User(3);
        order = new UserOrder();
    }

    @Test
    void findUserOrderTest() {
        when(userDao.findById(3L)).thenReturn(Optional.of(user));
        when(orderDao.findByIdAndUser(100, user)).thenReturn(Optional.of(order));

        final UserOrder userOrder = service.findUserOrder(3, 100);
        verify(userDao).findById(3L);
        verify(orderDao).findByIdAndUser(100, user);
        Assertions.assertNotNull(userOrder);
    }

    @Test
    void findAllUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.findAll(0, 0));
    }

    @Test
    void findByIdUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.findById(1));
    }

    @Test
    void deleteUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.delete(1));
    }
}
