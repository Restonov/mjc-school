package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.builder.UserOrderBuilder;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Transactional
@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserOrderDaoTest {
    private User user;
    private GiftCertificate certificate;
    private UserOrder order;

    @Autowired
    private UserOrderDao dao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void migrateFlyway() {
        flyway.migrate();
    }

    @BeforeEach
    void setUpOrder() {
        user = new User(10, "Oksana");
        Set<GiftCertificateTag> tags = new HashSet<>();
        tags.add(new GiftCertificateTag("testTag"));
        certificate = new GiftCertificate("cer1",
                "Test cert", BigDecimal.valueOf(150),
                30, LocalDateTime.now(), LocalDateTime.now(), tags);
        certificate.setId(10);
        order = UserOrderBuilder.build(user, certificate);
    }

    @Test
    void findByIdUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> dao.findById(0));
    }

    @Test
    void addOrderTest() {
        UserOrder userOrder = dao.add(order);
        Assertions.assertNotNull(userOrder);
    }

    @AfterEach
    public void tearDownOrder() {
        user = null;
        certificate = null;
        order = null;
    }

    @Test
    void deleteUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> dao.delete(0));
    }
}