package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOrderDao extends JpaRepository<UserOrder, Long> {

    /**
     * Find All User's Orders
     *
     * @param user     User
     * @param pageable Num and size of results
     * @return Page of Orders
     */
    Page<UserOrder> findAllByUser(User user, Pageable pageable);

    /**
     * Find User's certain Order
     *
     * @param orderId Order ID
     * @param user    User
     * @return Optional Order
     */
    Optional<UserOrder> findByIdAndUser(long orderId, User user);
}
