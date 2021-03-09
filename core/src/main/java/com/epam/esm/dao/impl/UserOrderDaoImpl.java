package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.UserOrder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserOrderDaoImpl extends UserOrderDao {

    @Override
    public Optional<UserOrder> findById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserOrder add(UserOrder order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException();
    }
}
