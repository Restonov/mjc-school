package com.epam.esm.service.builder;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;

import java.time.LocalDateTime;

public class UserOrderBuilder {

    private UserOrderBuilder() {
    }

    public static UserOrder build(User user, GiftCertificate certificate){
        UserOrder order = new UserOrder(user, certificate);
        order.setCost(certificate.getPrice());
        order.setPurchaseDate(LocalDateTime.now());
        return order;
    }
}
