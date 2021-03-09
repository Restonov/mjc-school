package com.epam.esm.service;

import static org.mockito.Mockito.*;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.impl.GiftCertificateTagDaoImpl;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.impl.GiftCertificateTagServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateTagServiceTest {
    GiftCertificateTagService service;
    GiftCertificateTagDao dao;
    GiftCertificateTag tag;
    List<GiftCertificateTag> tags;

    @BeforeAll
    void setUp() {
        dao = mock(GiftCertificateTagDaoImpl.class);
        service = new GiftCertificateTagServiceImpl(dao);
        tag = new GiftCertificateTag(1, "tag1");
        tags = new ArrayList<>();
        tags.add(tag);
    }

    @AfterAll
    void tearDown() {
        dao = null;
        service = null;
        tag = null;
        tags = null;
    }

    @Test
    void createTagPositiveTest() {
        String tagName = "tag1";
        when(dao.findByName(tagName)).thenThrow(new DataAccessResourceFailureException(""));
        when(dao.add(tag)).thenReturn(tag);

        GiftCertificateTag expected = service.create(tag);
        verify(dao).add(tag);
        Assertions.assertEquals(expected, tag);
    }

    @Test
    void findAllPositiveTest() {
        when(dao.findAll()).thenReturn(tags);

        List<GiftCertificateTag> tags = service.findAll();
        verify(dao, times(1)).findAll();
        Assertions.assertFalse(tags.isEmpty());
    }

    @Test
    void findByIdPositiveTest() {
        long id = 1;
        when(dao.findById(id)).thenReturn(Optional.of(tag));

        Optional<GiftCertificateTag> optional = service.findById(id);
        verify(dao).findById(id);
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    void deletePositiveTest() {
        long id = 1;
        doNothing().when(dao).delete(id);

        service.delete(id);
        verify(dao).delete(id);
    }
}
