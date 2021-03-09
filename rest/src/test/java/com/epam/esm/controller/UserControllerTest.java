package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.AfterAll;
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
class UserControllerTest {
    private static final String USERS_PATH = "/users";
    private UserService userService;
    private UserOrderService orderService;
    private User user;
    private UserOrder order;

    @BeforeAll
    void initialiseRestAssuredMockMvcStandalone() {
        userService = mock(UserService.class);
        orderService = mock(UserOrderService.class);
        RestAssuredMockMvc.standaloneSetup(new UserController(userService, orderService));
    }

    @AfterAll
    void tearDown() {
        userService = null;
        orderService = null;
        user = null;
        order = null;
    }

    @Test
    void findAllTest() {
        when(userService.findAll(1)).thenReturn(new ArrayList<>());
        given().when()
                .get(USERS_PATH + "?page=1")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }

    @BeforeEach
    void setUpOrder() {
        order = new UserOrder();
        order.setId(1);
    }

    @Test
    void createOrderTest() {
        when(orderService.create(order)).thenReturn(order);

        MockMvcResponse response = with().contentType(MediaType.APPLICATION_JSON_VALUE).body(order)
                .when().post(USERS_PATH + "/" + 0 + "/orders");
        response.then().statusCode(HttpStatus.CREATED.value());

        int id = response.path("id");
        Assertions.assertEquals(1, id);
    }

    @BeforeEach
    void setUpUserForFindByIdTest() {
        user = new User();
        user.setId(101);
        user.setName("User");
    }

    @Test
    void findByIdTest() {
        long id = 101;
        when(userService.findById(id)).thenReturn(user);

        given()
                .when()
                .get(USERS_PATH + "/" + id)
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void findAllOrdersTest() {
        when(userService.findAllOrders(1, 1)).thenReturn(new ArrayList<>());
        given().when()
                .get(USERS_PATH + "/1/orders?page=1")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void findOrderTest() {
        when(userService.findOrder(1, 1)).thenReturn(new UserOrder());
        given().when()
                .get(USERS_PATH + "/1/orders/1")
                .then()
                .statusCode(HttpStatus.FOUND.value());
    }
}
