package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends UserService {

    /**
     * Constructor for test purposes
     *
     * @param dao User dao
     */
    public UserServiceImpl(UserDao dao) {
        this.userDao = dao;
    }

    @Override
    public User create(User resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll(int currentPage) {
        return userDao.findAll(currentPage, User.class);
    }

    @Override
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
    public boolean delete(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserOrder> findAllOrders(int page, long userId) {
        return userDao.findAllOrders(page, userId);
    }

    @Override
    public UserOrder findOrder(long userId, long orderId) {
        UserOrder order;
        Optional<UserOrder> optionalOrder = userDao.findOrder(userId, orderId);
        if ( optionalOrder.isPresent() ){
            order = optionalOrder.get();
        } else {
            throw new ResourceNotFoundException("order.not.found");
        }
        return order;
    }
}
