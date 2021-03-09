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
    }

    @BeforeEach
    void setUpCreateTest() {
        user = mock(User.class);
        certificate = mock(GiftCertificate.class);
        order = mock(UserOrder.class);
    }

    @Test
    void createTest() {
        when(order.getUser()).thenReturn(user);
        when(order.getCertificate()).thenReturn(certificate);
        when(user.getId()).thenReturn(1L);
        when(certificate.getId()).thenReturn(1L);
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificate));
        when(orderDao.add(order)).thenReturn(order);

        service.create(order);
        verify(userDao).findById(1);
        verify(certificateDao).findById(1);
        verify(orderDao).add(order);
    }

    @Test
    void findAllUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.findAll(1));
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
