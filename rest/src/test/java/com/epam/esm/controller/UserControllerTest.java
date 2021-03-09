package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private static final String USERS_PATH = "/api/users";
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

    @BeforeEach
    void setUpUserForFindByIdTest() {
        user = new User();
        user.setId(101);
        user.setName("User");
        user.setRole(User.Role.ROLE_USER);
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
}
