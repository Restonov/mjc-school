package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.GiftCertificateService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateControllerTest {
    private static final String CERTIFICATES_PATH = "/api/certificates";
    private GiftCertificate certificate;
    private List<GiftCertificate> certificates;
    private GiftCertificateService service;

    @BeforeAll
    void initialiseRestAssuredMockMvcStandalone() {
        service = mock(GiftCertificateService.class);
        RestAssuredMockMvc.standaloneSetup(new GiftCertificateController(service));
    }

    @AfterAll
    void tearDown() {
        service = null;
        certificate = null;
        certificates = null;
    }

    @Test
    void findAllTest() {
        when(service.findAll(0, 10)).thenReturn(new PageImpl<>(new ArrayList<>()));
        given().when()
                .get(CERTIFICATES_PATH + "?page=0&size=10")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }

    @BeforeEach
    void setUpCertForFindByTagNameTest() {
        certificate = new GiftCertificate();
        Set<GiftCertificateTag> tags = new HashSet<>();
        tags.add(new GiftCertificateTag(1, "tag"));
        certificate.setTags(tags);
        certificates = new ArrayList<>();
        certificates.add(certificate);
    }

    @Test
    void findByTagNameTest() {
        String tag = "tag";
        when(service.findByTagNames(0, 10, tag)).thenReturn(new PageImpl<>(certificates));
        given()
                .when()
                .get(CERTIFICATES_PATH + "?page=0" + "&tags=" + tag)
                .then().statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void deleteTest() {
        long id = 5;
        given().when()
                .delete(CERTIFICATES_PATH + "/" + id)
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
