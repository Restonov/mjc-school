package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.Constants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
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
import org.springframework.data.domain.Sort;

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
    private GiftCertificateServiceImpl service;
    private GiftCertificateDao certDao;
    private GiftCertificateTagDao tagDao;
    private GiftCertificate certificate;
    private GiftCertificate certificate2;
    private GiftCertificateTag tag;
    private Set<GiftCertificateTag> tags;
    private Set<GiftCertificate> tagCertificates;
    private List<GiftCertificate> certificates;
    private Page<GiftCertificate> certificatesPage;
    private Pageable pageAndResultPerPage;
    private Pageable pageSortedResult;

    @BeforeAll
    void setUp() {
        certDao = mock(GiftCertificateDao.class);
        tagDao = mock(GiftCertificateTagDao.class);
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
        certificatesPage = null;
        pageAndResultPerPage = null;
        pageSortedResult = null;
    }

    @BeforeEach
    void setUpCert() {
        certificate = new GiftCertificate("Test cert",
                "Cert for test purpose", new BigDecimal(100), 30, new HashSet<>());
        tag = new GiftCertificateTag(1, "testTag");
        tagCertificates = new HashSet<>();
        tag.setCertificates(tagCertificates);
        tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);
    }

    @Test
    void createCertificatePositiveTest() {
        when(certDao.save(certificate)).thenReturn(certificate);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(tagDao.save(tag)).thenReturn(tag);

        GiftCertificate expectedCert = service.create(certificate);
        verify(certDao).save(certificate);
        Assertions.assertNotNull(expectedCert);
    }

    @BeforeEach
    void setUpCertificates() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate(1L, "Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
        certificates.add(certificate);
        pageAndResultPerPage = PageRequest.of(0, 10);

        certificatesPage = new PageImpl<>(certificates, pageAndResultPerPage, 100L);
    }

    @Test
    void findAllTest() {
        when(certDao.findAll(pageAndResultPerPage)).thenReturn(certificatesPage);

        Page<GiftCertificate> actual = service.findAll(0, 10);
        verify(certDao).findAll(pageAndResultPerPage);
        Assertions.assertFalse(actual.isEmpty());
    }

    @BeforeEach
    void setUpSortCertificates() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate(1L, "Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
        certificate = new GiftCertificate(2L, "X Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
        certificates.add(certificate);
        certificates.add(certificate2);

        pageSortedResult = PageRequest.of(0, 5, Sort.by(Constants.NAME).ascending());
        certificatesPage = new PageImpl<>(certificates, pageSortedResult, 2);
    }

    @Test
    void findAllAndSortTest() {
        when(certDao.findAll(pageSortedResult)).thenReturn(certificatesPage);

        Page<GiftCertificate> actual = service.findAllAndSort(0, 5, "name_asc");
        verify(certDao).findAll(pageSortedResult);
        Assertions.assertEquals("Test cert" , actual.getContent().get(0).getName());
    }

    @BeforeEach
    void setUpCertWithId() {
        certificate = new GiftCertificate(3L, "Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
    }

    @Test
    void findByIdTest() {
        when(certDao.findById(3L)).thenReturn(Optional.of(certificate));

        GiftCertificate actualCert = service.findById(3);
        verify(certDao).findById(3L);
        Assertions.assertNotNull(actualCert);
    }

    @Test
    void findByTagNameTest() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate(1L, "Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
        certificates.add(certificate);
        tags = new HashSet<>();
        tag = new GiftCertificateTag(1, "test");
        tags.add(tag);

        pageAndResultPerPage = PageRequest.of(0, 10);
        certificatesPage = new PageImpl<>(certificates, pageAndResultPerPage, 5);

        when(tagDao.findByName("test")).thenReturn(Optional.of(tag));
        when(certDao.findByTagsIn(tags, pageAndResultPerPage)).thenReturn(certificatesPage);

        Page<GiftCertificate> certificates = service.findByTagNames(0, 10, "test");
        verify(certDao).findByTagsIn(tags, pageAndResultPerPage);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @BeforeEach
    void setUpCertWithKeyWord() {
        certificates = new ArrayList<>();
        certificate = new GiftCertificate(1L, "Test cert",
                "Cert for test purpose", new BigDecimal(100), 30,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(), new HashSet<>()
        );
        certificates.add(certificate);

        pageAndResultPerPage = PageRequest.of(0, 10);
        certificatesPage = new PageImpl<>(certificates, pageAndResultPerPage, 5);
    }

    @Test
    void findByKeyWord() {
        when(certDao
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase
                        ("test", "test", pageAndResultPerPage))
                .thenReturn(certificatesPage);

        Page<GiftCertificate> actualCerts = service.findByKeyWord(0, 10, "test");
        Assertions.assertFalse(actualCerts.isEmpty());
    }

    @Test
    void deleteTest() {
        certificate = new GiftCertificate();
        certificate.setId(1L);
        when(certDao.findById(1L)).thenReturn(Optional.of(certificate));

        service.delete(1L);

        verify(certDao).findById(1L);
        verify(certDao).delete(certificate);
    }

    @Test
    void createCertificateTagRelationTest() {
        certificate = new GiftCertificate();
        tags = new HashSet<>();
        tag = new GiftCertificateTag("someTag");

        tag.setCertificates(new HashSet<>());
        tags.add(tag);
        certificate.setTags(tags);

        when(tagDao.findByName("someTag")).thenReturn(Optional.of(tag));

        final Set<GiftCertificateTag> certificateTagRelation = service.createCertificateTagRelation(certificate);
        verify(tagDao).findByName("someTag");
        Assertions.assertFalse(certificateTagRelation.isEmpty());
    }

    @Test
    void createCertificateTagRelationViaTagSaveTest() {
        certificate = new GiftCertificate();
        tags = new HashSet<>();
        tag = new GiftCertificateTag("justTag");

        tag.setCertificates(new HashSet<>());
        tags.add(tag);
        certificate.setTags(tags);

        when(tagDao.findByName("justTag")).thenReturn(Optional.empty());
        when(tagDao.save(tag)).thenReturn(tag);

        final Set<GiftCertificateTag> certificateTagRelation = service.createCertificateTagRelation(certificate);
        verify(tagDao).findByName("justTag");
        verify(tagDao).save(tag);
        Assertions.assertFalse(certificateTagRelation.isEmpty());
    }
}
