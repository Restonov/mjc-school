package com.epam.esm.service;

import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserService extends AbstractService<User> {

    @Autowired
    protected PasswordEncoder passwordEncoder;
}
