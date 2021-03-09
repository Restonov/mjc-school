package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserOrderServiceImpl extends UserOrderService {
    private static final String USER_NOT_FOUND = "user.not.found";

    /**
     * Constructor for test purposes
     *
     * @param orderDao       Order dao
     * @param userDao        User dao
     * @param certificateDao Certificate dao
     */
    public UserOrderServiceImpl(UserOrderDao orderDao,
                                UserDao userDao,
                                GiftCertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public UserOrder create(UserOrder order) {
        UserOrder newOrder;
        long userId = order.getUser().getId();
        final Optional<User> optionalUser = userDao.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        long certificateId = order.getCertificate().getId();
            Optional<GiftCertificate> optionalCert = certificateDao.findById(certificateId);
            if (optionalCert.isPresent()) {
                order.setUser(user);
                order.setCertificate(optionalCert.get());
                order.setCost(optionalCert.get().getPrice());
                newOrder = orderDao.save(order);
            } else {
                throw new ResourceNotFoundException("certificate.not.found");
            }
        return newOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserOrder> findUserOrders(int currentPage, int pageSize, long userId) {
        final Optional<User> optionalUser = userDao.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        Pageable pageAndResultPerPage = PageRequest.of(currentPage, pageSize);
        return orderDao.findAllByUser(user, pageAndResultPerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public UserOrder findUserOrder(long userId, long orderId) {
        final Optional<User> optionalUser = userDao.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        final Optional<UserOrder> optionalOrder = orderDao.findByIdAndUser(orderId, user);
        return optionalOrder.orElseThrow(() -> new ResourceNotFoundException("order.not.found"));
    }

    @Override
    public Page<UserOrder> findAll(int page, int pageSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserOrder findById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }
}
