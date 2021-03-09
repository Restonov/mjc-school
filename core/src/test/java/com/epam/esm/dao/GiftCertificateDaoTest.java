package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificate;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ActiveProfiles(profiles = "qa")
@Transactional
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateDaoTest {
    private GiftCertificate certificate;

    @Autowired
    GiftCertificateDao dao;

    @Autowired
    Flyway flyway;

    @BeforeAll
    void setUp() {
        flyway.migrate();
        certificate = new GiftCertificate(
                "cer1", "Test cert", BigDecimal.valueOf(150), 30,
                LocalDateTime.of(2021, 1, 13, 9, 15),
                LocalDateTime.of(2021, 1, 13, 9, 15)
        );
    }

    @AfterAll
    void tearDown() {
        certificate = null;
    }

    @Test
    void addCertificateTest() {
        GiftCertificate expected = dao.add(certificate);
        Assertions.assertEquals(certificate.getName(), expected.getName());
    }

    @Test
    void findAllTest() {
        List<GiftCertificate> certificates = dao.findAll();
        Assertions.assertEquals("Zara", certificates.get(0).getName());
    }

    @Test
    void findByIdTest() {
        Optional<GiftCertificate> certificate = dao.findById(10);
        Assertions.assertTrue(certificate.isPresent()
                && certificate.get().getName().equals("TyRent"));
    }

    @Test
    void findByTagId() {
        List<GiftCertificate> certificates = dao.findByTagId(2);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    void findByKeywordTest() {
        List<GiftCertificate> certificates = dao.findByKeyword("%ar%");
        Assertions.assertFalse(certificates.isEmpty());
    }
}
