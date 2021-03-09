package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    /**
     * Find Users ordered by sum of Orders DESC
     * First result will be the most profitable User
     *
     * @return Users
     */
    @Query("SELECT u FROM UserOrder o, User u WHERE o.user = u group by u ORDER BY SUM(o.cost) DESC")
    List<User> findUsersBySumOfOrdersDesc();

    /**
     * Find User by Name
     *
     * @param username User name
     * @return Optional of User
     */
    Optional<User> findByName(String username);
}
