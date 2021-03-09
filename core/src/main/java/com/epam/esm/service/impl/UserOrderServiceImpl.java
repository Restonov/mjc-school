package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserOrderServiceImpl extends UserOrderService {

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
    public UserOrder create(UserOrder orderDto) {
        UserOrder newOrder;
        long userId = orderDto.getUser().getId();
        long certificateId = orderDto.getCertificate().getId();
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            Optional<GiftCertificate> optionalCert = certificateDao.findById(certificateId);
            if (optionalCert.isPresent()) {
                orderDto.setUser(optionalUser.get());
                orderDto.setCertificate(optionalCert.get());
                orderDto.setCost(optionalCert.get().getPrice());
                newOrder = orderDao.add(orderDto);
            } else {
                throw new ResourceNotFoundException("certificate.not.found");
            }
        } else {
            throw new ResourceNotFoundException("user.not.found");
        }
        return newOrder;
    }

    @Override
    public List<UserOrder> findAll(int page) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserOrder findById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException();
    }
}
