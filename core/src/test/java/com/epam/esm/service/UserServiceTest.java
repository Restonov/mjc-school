package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.service.impl.UserServiceImpl;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private UserService service;
    private UserDao dao;
    private List<User> users;
    private User user;
    private Pageable pageAndResultPerPage;
    private Page<User> userPage;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void beforeAll() {
        dao = mock(UserDao.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new UserServiceImpl(dao, passwordEncoder);
    }

    @AfterAll
    void afterAll() {
        service = null;
        dao = null;
        users = null;
        user = null;
        pageAndResultPerPage = null;
        userPage = null;
    }

    @BeforeEach
    void setUpCreateTest() {
        user = new User("User", "password");
    }

    @Test
    void createTest() {
        when(dao.findByName("User")).thenReturn(Optional.of(user));
        when(dao.save(user)).thenReturn(user);

        final User createdUser = service.create(this.user);
        Assertions.assertNotNull(createdUser);
    }

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        user = new User(2, "Gleb");
        users.add(user);

        pageAndResultPerPage = PageRequest.of(0, 10);
        userPage = new PageImpl<>(users, pageAndResultPerPage, 5);
    }

    @Test
    void findAllTest() {
        when(dao.findAll(pageAndResultPerPage)).thenReturn(userPage);
        Page<User> allUsers = service.findAll(0, 10);
        Assertions.assertFalse(allUsers.isEmpty());
    }

    @BeforeEach
    void setUpUser() {
        user = new User();
    }

    @Test
    void findByIdTest() {
        when(dao.findById(1L)).thenReturn(Optional.of(user));
        User actualUser = service.findById(1);
        Assertions.assertNotNull(actualUser);
    }

    @Test
    void deleteUnsupportedTest() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.delete(0));
    }

    @BeforeEach
    void setUpLoadUserByUsernameTest() {
        user = new User("Username");
    }

    @Test
    void loadUserByUsernameTest() {
        when(dao.findByName("Username")).thenReturn(Optional.of(user));

        final UserDetails userDetails = service.loadUserByUsername("Username");
        Assertions.assertNotNull(userDetails);
    }
}
