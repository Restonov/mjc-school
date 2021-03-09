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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private Page<GiftCertificateTag> tagPage;
    private Pageable pageAndResultPerPage;

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
        tagPage = null;
        pageAndResultPerPage = null;
    }

    @BeforeEach
    void setUpTag() {
        tag = new GiftCertificateTag("testTag");
    }

    @Test
    void createTest() {
        when(tagDao.save(tag)).thenReturn(tag);
        GiftCertificateTag actual = service.create(tag);
        Assertions.assertNotNull(actual);
    }

    @BeforeEach
    void setUpTags() {
        tags = new ArrayList<>();
        tag = new GiftCertificateTag("testTag");
        tags.add(tag);

        pageAndResultPerPage = PageRequest.of(0, 10);
        tagPage = new PageImpl<>(tags, pageAndResultPerPage, 5);
    }

    @Test
    void findAllTest() {
        when(tagDao.findAll(pageAndResultPerPage)).thenReturn(tagPage);
        Page<GiftCertificateTag> allTags = service.findAll(0, 10);
        verify(tagDao).findAll(pageAndResultPerPage);
        Assertions.assertFalse(allTags.isEmpty());
    }

    @BeforeEach
    void setUpIdTag() {
        tag = new GiftCertificateTag("testTag");
    }

    @Test
    void findById() {
        when(tagDao.findById(101L)).thenReturn(Optional.of(tag));
        GiftCertificateTag actualTag = service.findById(101);
        verify(tagDao).findById(101L);
        Assertions.assertNotNull(actualTag);
    }

    @BeforeEach
    void setUpUserWithOrders() {
        user = new User(1, "Boris");

        certificate = new GiftCertificate("Cert",
                "Cert for test purpose", new BigDecimal(100), 30, new HashSet<>());
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
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userDao.findUsersBySumOfOrdersDesc()).thenReturn(users);
        GiftCertificateTag mostProfitableTag = service.findMostProfitableTag();
        verify(userDao).findUsersBySumOfOrdersDesc();
        Assertions.assertEquals("tag", mostProfitableTag.getName());
    }

    @Test
    void deleteTest() {
        tag = new GiftCertificateTag();
        tag.setId(1L);
        when(tagDao.findById(1L)).thenReturn(Optional.of(tag));

        service.delete(1L);

        verify(tagDao).findById(1L);
        verify(tagDao).delete(tag);
    }


}
