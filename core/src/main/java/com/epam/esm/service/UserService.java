package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;

import java.util.List;

public abstract class UserService extends AbstractService<User> {

    /**
     * Find all User's orders
     *
     * @param page   page in URL
     * @param userId User id
     * @return Orders
     */
    public abstract List<UserOrder> findAllOrders(int page, long userId);

    /**
     * Find User's order
     *
     * @param userId  User id
     * @param orderId Order id
     * @return Order
     */
    public abstract UserOrder findOrder(long userId, long orderId);

}
