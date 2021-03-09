package com.epam.esm.dao;

import com.epam.esm.config.JUnitConfig;
import com.epam.esm.entity.GiftCertificateTag;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ActiveProfiles(profiles = "qa")
@SpringJUnitConfig
@Transactional
@ContextConfiguration(classes = JUnitConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateTagDaoTest {
    private GiftCertificateTag tag;

    @Autowired
    private GiftCertificateTagDao dao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void beforeAll() {
        flyway.migrate();
        tag = new GiftCertificateTag("testTag");
    }

    @AfterAll
    void afterAll() {
        tag = null;
    }

    @Test
    void addTest() {
        GiftCertificateTag expected = dao.add(tag);
        Assertions.assertNotNull(expected);
    }

    @Test
    void findAllTest() {
        List<GiftCertificateTag> tags = dao.findAll();
        Assertions.assertFalse(tags.isEmpty());
    }

    @Test
    void findByIdTest() {
        Optional<GiftCertificateTag> optionalTag = dao.findById(100);
        Assertions.assertTrue(optionalTag.isPresent()
                && optionalTag.get().getName().equals("sport"));
    }

    @Test
    void findByNameTest() {
        Optional<GiftCertificateTag> optionalTag = dao.findByName("hobby");
        Assertions.assertTrue(optionalTag.isPresent()
                && optionalTag.get().getName().equals("hobby"));
    }

    @Test
    void findByCertificateIdTest() {
        List<GiftCertificateTag> tags = dao.findByCertificateId(101);
        Assertions.assertFalse(tags.isEmpty());
    }
}
