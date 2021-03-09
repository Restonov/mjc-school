package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Primary
@Transactional
public class UserServiceImpl extends UserService implements UserDetailsService {

    /**
     * Constructor for test purposes
     *
     * @param dao User dao
     */
    public UserServiceImpl(UserDao dao, PasswordEncoder passwordEncoder) {
        this.userDao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User userEntity) {
        User createdUser;
        final Optional<User> userWithExistingName = userDao.findByName(userEntity.getName());
        if (!userWithExistingName.isPresent()) {
            String encryptedPassword = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(encryptedPassword);
            createdUser = userDao.save(userEntity);
        } else {
            throw new ServiceException("user.already.registered");
        }
        return createdUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(int currentPage, int pageSize) {
        Pageable pageAndResultPerPage = PageRequest.of(currentPage, pageSize);
        return userDao.findAll(pageAndResultPerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(long id) {
        User user;
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new ResourceNotFoundException("user.not.found");
        }
        return user;
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * User details method for security purposes
     * Return User if exists in DB
     * Save new User if registered with OAUTH and return it
     *
     * @param username User's name
     * @return User
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userDao.findByName(username);
        return optionalUser.orElseGet(() -> userDao.save(new User(username)));
    }
}
