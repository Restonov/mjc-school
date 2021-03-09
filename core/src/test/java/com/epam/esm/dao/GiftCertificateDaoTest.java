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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
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
    private Pageable pageWithResult;

    @Autowired
    private GiftCertificateDao certificateDao;

    @Autowired
    private GiftCertificateTagDao tagDao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void setUp() {
        flyway.migrate();
    }

    @AfterAll
    void tierDown() {
        certificate = null;
        tags = null;
        pageWithResult = null;
    }

    @BeforeEach
    void setUpAddCert() {
        tags = new HashSet<>();
        tags.add(new GiftCertificateTag("testTag"));
        certificate = new GiftCertificate(
                "Cert1", "Test cert", BigDecimal.valueOf(100), 10, tags);
    }

    @Test
    void addCertificateTest() {
        GiftCertificate cert = certificateDao.save(certificate);
        Assertions.assertEquals("Cert1", cert.getName());
    }

    @Test
    void findAllTest() {
        List<GiftCertificate> certificates = certificateDao.findAll();
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    void findByIdTest() {
        Optional<GiftCertificate> optional = certificateDao.findById(10L);
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
        pageWithResult = PageRequest.of(0, 5);
    }

    @Test
    void findByTagNameTest() {
        Page<GiftCertificate> certificates = certificateDao.findByTagsIn(tags, pageWithResult);
        Assertions.assertEquals("V-Kart", certificates.getContent().get(0).getName());
    }

    @Test
    void findByKeywordTest() {
        Page<GiftCertificate> certificates = certificateDao
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("ent", "ent", pageWithResult);
        Assertions.assertEquals("TyRent", certificates.getContent().get(0).getName());
    }

    @Test
    void deleteTest() {
        final Optional<GiftCertificate> optional = certificateDao.findById(2L);
        optional.ifPresent(c -> certificateDao.delete(c));

        final Optional<GiftCertificate> secondCheck = certificateDao.findById(2L);
        Assertions.assertFalse(secondCheck.isPresent());
    }
}
