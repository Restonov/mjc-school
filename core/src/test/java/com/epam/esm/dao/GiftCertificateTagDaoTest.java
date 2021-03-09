package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.ResourceNotFoundException;
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

import java.util.Optional;

@Transactional
@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig(JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateTagDaoTest {
    private GiftCertificateTag testTag;

    @Autowired
    private GiftCertificateTagDao dao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void migrateFlyway() {
        flyway.migrate();
    }

    @BeforeEach
    void setUpTag() {
        testTag = new GiftCertificateTag("test");
    }

    @Test
    void addTest() {
        GiftCertificateTag tag = dao.save(testTag);
        Assertions.assertEquals("test", tag.getName());
    }

    @AfterEach
    void tearDown() {
        testTag = null;
    }

    @Test
    void findByIdTest() {
        Optional<GiftCertificateTag> optional = dao.findById(100L);
        Assertions.assertEquals("sport",
                optional.orElseThrow(ResourceNotFoundException::new).getName());
    }

    @Test
    void findByNameTest() {
        Optional<GiftCertificateTag> optional = dao.findByName("sport");
        Assertions.assertEquals("sport",
                optional.orElseThrow(ResourceNotFoundException::new).getName());
    }

    @Test
    void deleteTest() {
        dao.deleteById(100L);
        final Optional<GiftCertificateTag> optionalTag = dao.findById(100L);
        Assertions.assertFalse(optionalTag.isPresent());
    }
}
