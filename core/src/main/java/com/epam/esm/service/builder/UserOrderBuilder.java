package com.epam.esm.service.builder;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Build User's order
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOrderBuilder {

    public static UserOrder build(User user, GiftCertificate certificate){
        UserOrder order = new UserOrder(user, certificate);
        order.setCost(certificate.getPrice());
        order.setPurchaseDate(LocalDateTime.now());
        return order;
    }
}
