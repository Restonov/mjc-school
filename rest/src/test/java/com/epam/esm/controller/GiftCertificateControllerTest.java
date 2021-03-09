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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.with;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateControllerTest {
    private static final String CERTIFICATES_PATH = "/certificates";
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
        when(service.findAll(1)).thenReturn(new ArrayList<>());
        given().when()
                .get(CERTIFICATES_PATH + "?page=1")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }

    @BeforeEach
    void setUp() {
        certificate = new GiftCertificate();
        certificate.setId(1);
    }

    @Test
    void createTest() {
        when(service.create(certificate)).thenReturn(certificate);

        with().contentType(MediaType.APPLICATION_JSON_VALUE).body(certificate)
                .when().post(CERTIFICATES_PATH)
                .then().statusCode(HttpStatus.CREATED.value())
                .assertThat().body("id", equalTo(1));
    }

    @BeforeEach
    void setUpCertForFindByIdTest() {
        certificate = new GiftCertificate();
        certificate.setId(101);
        certificate.setName("Test");
    }

    @Test
    void findByIdTest() {
        long id = 101;
        when(service.findById(id)).thenReturn(certificate);

        given()
                .when()
                .get(CERTIFICATES_PATH + "/" + id)
                .then().statusCode(HttpStatus.OK.value());
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
        when(service.findByTagNames(1, tag)).thenReturn(certificates);

        given()
                .when()
                .get(CERTIFICATES_PATH + "?page=1" + "&tags=" + tag)
                .then().statusCode(HttpStatus.FOUND.value());
    }

    @BeforeEach
    void setUpCertForFindByKeyWordTest() {
        certificate = new GiftCertificate();
        certificate.setName("Some name");
        certificates = new ArrayList<>();
        certificates.add(certificate);
    }

    @Test
    void findByKeyWordTest() {
        String name = "Some name";
        when(service.findByTagNames(1, name)).thenReturn(certificates);

        given()
                .when()
                .get(CERTIFICATES_PATH + "?page=1" + "&search=" + name)
                .then().statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void deleteTest() {
        long id = 5;
        when(service.delete(id)).thenReturn(Boolean.TRUE);

        given().when()
                .delete(CERTIFICATES_PATH + "/" + id)
                .then().statusCode(HttpStatus.OK.value());
    }
}
