package com.epam.esm.service;

import static org.mockito.Mockito.*;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.GiftCertificateTagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceTest {
    GiftCertificateService service;
    GiftCertificateDao certDao;
    GiftCertificateTagDao tagDao;
    GiftCertificate certificate1;
    GiftCertificate certificate2;
    GiftCertificateTag tag;
    List<GiftCertificate> certificates;
    List<GiftCertificateTag> tags;

    @BeforeAll
    void setUp() {
        certDao = mock(GiftCertificateDaoImpl.class);
        tagDao = mock(GiftCertificateTagDaoImpl.class);
        service = new GiftCertificateServiceImpl(certDao, tagDao);

        certificate1 = new GiftCertificate(
                101, "cer1",
                LocalDateTime.of(2021, 1, 13, 9, 15)
        );
        certificate2 = new GiftCertificate(
                202, "cer2",
                LocalDateTime.of(2021, 2, 15, 13, 27)
        );

        tag = new GiftCertificateTag(1, "testTag");

        certificates = new ArrayList<>();
        certificates.add(certificate1);
        certificates.add(certificate2);
        tags = new ArrayList<>();
        tags.add(tag);
    }

    @AfterAll
    void tearDown() {
        certDao = null;
        tagDao = null;
        certificate1 = null;
        certificate2 = null;
        tag = null;
        certificates = null;
        tags = null;
        service = null;
    }

    @Test
    void createCertificatePositiveTest() {
        when(certDao.add(certificate1)).thenReturn(certificate1);

        GiftCertificate expectedCert = service.create(certificate1);
        verify(certDao).add(certificate1);
        Assertions.assertNotNull(expectedCert);
    }

    @Test
    void findAllPositiveTest() {
        when(certDao.findAll()).thenReturn(certificates);
        when(tagDao.findByCertificateId(certificate1.getCertificateId()))
                .thenReturn(tags);
        when(tagDao.findByCertificateId(certificate2.getCertificateId()))
                .thenReturn(tags);

        List<GiftCertificate> expected = service.findAll();
        verify(certDao).findAll();
        verify(tagDao).findByCertificateId(certificate1.getCertificateId());
        verify(tagDao).findByCertificateId(certificate2.getCertificateId());
        Assertions.assertFalse(expected.isEmpty());
    }

    @Test
    void findByIdPositiveTest() {
        long id = 101;
        when(certDao.findById(id)).thenReturn(Optional.of(certificate1));
        when(tagDao.findByCertificateId(id)).thenReturn(tags);

        Optional<GiftCertificate> optional = service.findById(id);
        verify(certDao).findById(id);
        verify(tagDao).findByCertificateId(id);
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    void findByTagNamePositiveTest() {
        String tagName = "testTag";
        long tagId = 1;
        when(tagDao.findByName(tagName)).thenReturn(Optional.of(tag));
        when(certDao.findByTagId(tagId)).thenReturn(certificates);

        List<GiftCertificate> certificates = service.findByTagName(tagName);
        verify(tagDao).findByName(tagName);
        verify(certDao).findByTagId(tagId);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    void updatePositiveTest() {
        long certId = 101;
        when(certDao.findById(certId)).thenReturn(Optional.of(certificate1));
        doNothing().when(certDao).update(certificate1);

        service.update(certificate1, certId);
        verify(certDao).findById(certId);
        verify(certDao).update(certificate1);
    }

    @Test
    void deletePositiveTest() {
        long certId = 101;
        doNothing().when(certDao).delete(certId);

        service.delete(certId);
        verify(certDao).delete(certId);
    }
}
