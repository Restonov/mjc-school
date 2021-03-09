package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.ResourceNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateDaoTest {
    private GiftCertificate certificate;
    private Set<GiftCertificateTag> tags;

    @Autowired
    private GiftCertificateDao certificateDao;

    @Autowired
    private GiftCertificateTagDao tagDao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void setUp() {
        flyway.migrate();
        Set<GiftCertificateTag> tags = new HashSet<>();
        tags.add(new GiftCertificateTag("testTag"));
        certificate = new GiftCertificate("cer1",
                "Test cert", BigDecimal.valueOf(150),
                30, LocalDateTime.now(), LocalDateTime.now(), tags);
    }

    @AfterAll
    void tierDown() {
        certificate = null;
        tags = null;
    }

    @Test
    void addCertificateTest() {
        GiftCertificate expected = certificateDao.add(certificate);
        Assertions.assertEquals(certificate.getName(), expected.getName());
    }

    @Test
    void findAllTest() {
        List<GiftCertificate> certificates = certificateDao.findAll(1, GiftCertificate.class);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    void findByIdTest() {
        Optional<GiftCertificate> optional = certificateDao.findById(10);
        Assertions.assertEquals("TyRent",
                optional.orElseThrow(ResourceNotFoundException::new).getName());
    }

    @BeforeEach
    void setUpTags() {
        tags = new HashSet<>();
        Optional<GiftCertificateTag> optionalTravelTag = tagDao.findByName("travel");
        optionalTravelTag.ifPresent(giftCertificateTag -> tags.add(giftCertificateTag));
        Optional<GiftCertificateTag> optionalHobbyTag = tagDao.findByName("hobby");
        optionalHobbyTag.ifPresent(giftCertificateTag -> tags.add(giftCertificateTag));
    }

    @Test
    void findByTagNameTest() {
        List<GiftCertificate> certificates = certificateDao.findByTags(1, tags);
        Assertions.assertEquals("TyRent", certificates.get(0).getName());
    }

    @Test
    void findByKeywordTest() {
        List<GiftCertificate> certificates = certificateDao.findByKeyword(1,"%rent%");
        Assertions.assertEquals("TyRent", certificates.get(0).getName());
    }

    @Test
    void deleteTest() {
        Assertions.assertTrue(certificateDao.delete(2));
    }
}
