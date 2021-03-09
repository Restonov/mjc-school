package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends UserDao {
    private static final String SELECT_ORDER_BY_ID = "SELECT o FROM UserOrder o WHERE o.id = :id AND o.user = :user";
    private static final String SELECT_USER_VIA_THE_MOST_ORDERS = "SELECT u FROM UserOrder o, User u WHERE o.user = u group by u ORDER BY SUM(o.cost) DESC";

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(User.class, id)
        );
    }

    @Override
    @SneakyThrows
    public User findMostProfitableUser() {
        User user;
        TypedQuery<User> query = entityManager.createQuery(SELECT_USER_VIA_THE_MOST_ORDERS, User.class);
        query.setMaxResults(NumberUtils.INTEGER_ONE);
        user = query.getSingleResult();
        return user;
    }

    @Override
    public User add(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserOrder> findAllOrders(int page, long userId) {
        List<UserOrder> orders;
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            CriteriaQuery<UserOrder> findQuery = criteriaBuilder.createQuery(UserOrder.class);
            Root<UserOrder> root = findQuery.from(UserOrder.class);
            Predicate userOrders = criteriaBuilder.equal(root.get(ParameterName.USER), user);

            findQuery.select(root).where(userOrders);
            TypedQuery<UserOrder> findCertificatesQuery = entityManager.createQuery(findQuery);

            paginateQuery(page, findCertificatesQuery);
            orders = findCertificatesQuery.getResultList();
        } else {
            throw new ResourceNotFoundException("user.not.found");
        }
        return orders;
    }

    @Override
    public Optional<UserOrder> findOrder(long userId, long orderId) {
        Optional<UserOrder> optionalOrder;
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            try {
                optionalOrder = Optional.ofNullable(entityManager.createQuery(SELECT_ORDER_BY_ID, UserOrder.class).
                        setParameter(ParameterName.ID, orderId)
                        .setParameter(ParameterName.USER, user)
                        .getSingleResult()
                );
            } catch (NoResultException e) {
                optionalOrder = Optional.empty();
            }
        } else {
            throw new ResourceNotFoundException("user.not.found");
        }
        return optionalOrder;
    }
}
