package com.epam.esm.security;

import com.epam.esm.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.epam.esm.entity.Constants.ADMINISTRATOR_AUTHORITY;

/**
 * Verify that User have rights for create and find certain Order
 *
 */
@Component
public class UserAccessVerification {

    public boolean verifyLoggedUser(User user) {
        boolean verified = false;
        final UserDetails loggedUser = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String userAuthority = loggedUser.getAuthorities().toString();
        if (loggedUser.getUsername().equals(user.getName()) || userAuthority.equals(ADMINISTRATOR_AUTHORITY)) {
            verified = true;
        }
        return verified;
    }
}
