package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.builder.UserOrderBuilder;
import com.epam.esm.service.impl.GiftCertificateTagServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateTagTest {
    private GiftCertificateTagDao tagDao;
    private UserDao userDao;
    private GiftCertificateTag tag;
    private GiftCertificateTagService service;
    private List<GiftCertificateTag> tags;
    private User user;
    private UserOrder order;
    private GiftCertificate certificate;

    @BeforeAll
    void setUp() {
        tagDao = mock(GiftCertificateTagDao.class);
        userDao = mock(UserDao.class);
        service = new GiftCertificateTagServiceImpl(tagDao, userDao);
    }

    @AfterAll
    void afterAll() {
        tagDao = null;
        userDao = null;
        service = null;
        tag = null;
        tags = null;
        user = null;
        order = null;
        certificate = null;
    }

    @BeforeEach
    void setUpTag() {
        tag = new GiftCertificateTag("testTag");
    }

    @Test
    void createTest() {
        when(tagDao.add(tag)).thenReturn(tag);
        GiftCertificateTag actual = service.create(tag);
        Assertions.assertNotNull(actual);
    }

    @BeforeEach
    void setUpTags() {
        tags = new ArrayList<>();
        tag = new GiftCertificateTag("testTag");
        tags.add(tag);
    }

    @Test
    void findAllTest() {
        when(tagDao.findAll(1, GiftCertificateTag.class)).thenReturn(tags);
        List<GiftCertificateTag> allTags = service.findAll(1);
        verify(tagDao).findAll(1, GiftCertificateTag.class);
        Assertions.assertFalse(allTags.isEmpty());
    }

    @BeforeEach
    void setUpIdTag() {
        tag = new GiftCertificateTag("testTag");
    }

    @Test
    void findById() {
        when(tagDao.findById(101)).thenReturn(Optional.of(tag));
        GiftCertificateTag actualTag = service.findById(101);
        verify(tagDao).findById(101);
        Assertions.assertNotNull(actualTag);
    }

    @BeforeEach
    void setUpUserWithOrders() {
        user = new User(1, "Boris");

        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30);
        Set<GiftCertificateTag> certificateTags = new HashSet<>();
        GiftCertificateTag certificateTag = new GiftCertificateTag(1, "tag");
        certificateTags.add(certificateTag);
        certificate.setTags(certificateTags);

        order = UserOrderBuilder.build(user, certificate);
        Set<UserOrder> orders = new HashSet<>();
        orders.add(order);
        user.setOrders(orders);
    }

    @Test
    void findMostProfitableTag() {
        when(userDao.findMostProfitableUser()).thenReturn(user);
        GiftCertificateTag mostProfitableTag = service.findMostProfitableTag();
        verify(userDao).findMostProfitableUser();
        Assertions.assertEquals("tag", mostProfitableTag.getName());
    }

    @Test
    void deleteTest() {
        when(tagDao.delete(1)).thenReturn(true);
        Assertions.assertTrue(tagDao.delete(1));
    }


}
