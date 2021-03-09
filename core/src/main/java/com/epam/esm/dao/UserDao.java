package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;

import java.util.List;
import java.util.Optional;

public abstract class UserDao extends AbstractDao<User> {

    /**
     * Find all User's orders
     *
     * @param page   page no in URL
     * @param userId User id
     * @return all User's orders
     */
    public abstract List<UserOrder> findAllOrders(int page, long userId);

    /**
     * Find User's order
     *
     * @param userId  User id
     * @param orderId Order id
     * @return chosen Order
     */
    public abstract Optional<UserOrder> findOrder(long userId, long orderId);

    /**
     * Find User that makes the most expensive orders
     *
     * @return most profitable User
     */
    public abstract User findMostProfitableUser();
}
