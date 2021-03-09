package com.epam.esm.service;

import com.epam.esm.entity.UserOrder;
import org.springframework.data.domain.Page;

public abstract class UserOrderService extends AbstractService<UserOrder> {


    /**
     * Find all User's orders
     *
     * @param page   page in URL
     * @param userId User id
     * @return Orders
     */
    public abstract Page<UserOrder> findUserOrders(int page, int pageSize, long userId);

    /**
     * Find User's order
     *
     * @param userId  User id
     * @param orderId Order id
     * @return Order
     */
    public abstract UserOrder findUserOrder(long userId, long orderId);
}
