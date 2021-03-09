package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.GiftCertificateTagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.util.QueryGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceTest {
    private GiftCertificateService service;
    private GiftCertificateDao certDao;
    private GiftCertificateTagDao tagDao;
    private GiftCertificate certificate;
    private GiftCertificate certificate2;
    private GiftCertificateTag tag;
    private Set<GiftCertificateTag> tags;
    private Set<GiftCertificate> tagCertificates;
    private List<GiftCertificate> certificates;

    @BeforeAll
    void setUp() {
        certDao = mock(GiftCertificateDaoImpl.class);
        tagDao = mock(GiftCertificateTagDaoImpl.class);
        service = new GiftCertificateServiceImpl(certDao, tagDao);
    }

    @AfterAll
    void tearDown() {
        certDao = null;
        tagDao = null;
        certificate = null;
        certificate2 = null;
        tagCertificates = null;
        tag = null;
        tags = null;
        service = null;
    }

    @BeforeEach
    void setUpCert() {
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30);
        tag = new GiftCertificateTag(1, "testTag");
        tagCertificates = new HashSet<>();
        tag.setCertificates(tagCertificates);
        tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);
    }

    @Test
    void createCertificatePositiveTest() {
        when(certDao.add(certificate)).thenReturn(certificate);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(tagDao.add(tag)).thenReturn(tag);

        GiftCertificate expectedCert = service.create(certificate);
        verify(certDao).add(certificate);
        Assertions.assertNotNull(expectedCert);
    }

    @BeforeEach
    void setUpCertificates() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificates.add(certificate);
    }


    @Test
    void findAllTest() {
        when(certDao.findAll(1, GiftCertificate.class)).thenReturn(certificates);

        List<GiftCertificate> actual = service.findAll(1);
        verify(certDao).findAll(1, GiftCertificate.class);
        Assertions.assertFalse(actual.isEmpty());
    }

    @BeforeEach
    void setUpSortCertificates() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificate2 = new GiftCertificate("X Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificates.add(certificate);
        certificates.add(certificate2);
    }

    @Test
    void findAllAndSortTest() {
        when(certDao.findAllAndSort(1, "name_asc")).thenReturn(certificates);

        List<GiftCertificate> actual = service.findAllAndSort(1, "name_asc");
        verify(certDao).findAllAndSort(1, "name_asc");
        Assertions.assertEquals("Test cert" , actual.get(0).getName());
    }

    @BeforeEach
    void setUpCertWithId() {
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificate.setId(3);
    }

    @Test
    void findByIdTest() {
        when(certDao.findById(3)).thenReturn(Optional.of(certificate));

        GiftCertificate actualCert = service.findById(3);
        verify(certDao).findById(3);
        Assertions.assertNotNull(actualCert);
    }

    @BeforeEach
    void setUpCertWithTag() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificates.add(certificate);
        tags = new HashSet<>();
        tag = new GiftCertificateTag(1, "test");
        tags.add(tag);
    }

    @Test
    void findByTagNameTest() {
        when(tagDao.findByName("test")).thenReturn(Optional.of(tag));
        when(certDao.findByTags(1, tags)).thenReturn(certificates);

        List<GiftCertificate> certificates = service.findByTagNames(1, "test");
        verify(certDao).findByTags(1, tags);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @BeforeEach
    void setUpCertWithKeyWord() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>()
        );
        certificates.add(certificate);
    }

    @Test
    void findByKeyWord() {
        when(certDao.findByKeyword(1, QueryGenerator.generateKeyWord("test"))).thenReturn(certificates);

        List<GiftCertificate> actualCerts = service.findByKeyWord(1, "test");
        verify(certDao).findByKeyword(1, QueryGenerator.generateKeyWord("test"));
        Assertions.assertFalse(actualCerts.isEmpty());
    }

    @Test
    void deleteTest() {
        when(certDao.delete(1)).thenReturn(true);
        Assertions.assertTrue(service.delete(1));
    }
}
