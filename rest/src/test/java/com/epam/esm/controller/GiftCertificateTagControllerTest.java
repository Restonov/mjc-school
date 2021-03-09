package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.GiftCertificateTagService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.with;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateTagControllerTest {
    private static final String TAGS_PATH = "/tags";
    private GiftCertificateTagService service;
    private GiftCertificateTag tag;

    @BeforeAll
    void initialiseRestAssuredMockMvcStandalone() {
        service = mock(GiftCertificateTagService.class);
        RestAssuredMockMvc.standaloneSetup(new GiftCertificateTagController(service));
    }

    @AfterAll
    void tearDown() {
        service = null;
        tag = null;
    }

    @Test
    void findAllTest() {
        when(service.findAll(1)).thenReturn(new ArrayList<>());
        given().when()
                .get(TAGS_PATH + "?page=1")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }

    @BeforeEach
    void setUp() {
        tag = new GiftCertificateTag();
        tag.setId(1);
        tag.setName("Tag");
    }

    @Test
    void createTest() {
        when(service.create(tag)).thenReturn(tag);

        MockMvcResponse response = with().contentType(MediaType.APPLICATION_JSON_VALUE).body(tag)
                .when().post(TAGS_PATH);
        response.then().statusCode(HttpStatus.CREATED.value());

        String name = response.path("name");
        Assertions.assertEquals("Tag", name);
    }

    @Test
    void findMostProfitableTest() {
        tag = new GiftCertificateTag();
        tag.setName("Profit");
        when(service.findMostProfitableTag()).thenReturn(tag);

        MockMvcResponse response = with().contentType(MediaType.APPLICATION_JSON_VALUE).body(tag)
                .when().get(TAGS_PATH + "/profit");
        response.then().statusCode(HttpStatus.FOUND.value());

        String name = response.path("name");
        Assertions.assertEquals("Profit", name);
    }

    @BeforeEach
    void setUpCertForFindByIdTest() {
        tag = new GiftCertificateTag();
        tag.setId(101);
        tag.setName("Test");
    }

    @Test
    void findByIdTest() {
        long id = 101;
        when(service.findById(id)).thenReturn(tag);

        given()
                .when()
                .get(TAGS_PATH + "/" + id)
                .then().statusCode(HttpStatus.OK.value());
    }


    @Test
    void deleteTest() {
        long id = 5;
        when(service.delete(id)).thenReturn(Boolean.TRUE);

        given().when()
                .delete(TAGS_PATH + "/" + id)
                .then().statusCode(HttpStatus.OK.value());
    }
}
