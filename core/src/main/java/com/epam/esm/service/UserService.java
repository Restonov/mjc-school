package com.epam.esm.service;

import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserService extends AbstractService<User> implements UserDetailsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;
}
