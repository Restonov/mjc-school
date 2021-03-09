package com.epam.esm.dao;

import com.epam.esm.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Dao layer main class
 * works with DB
 *
 */
public abstract class AbstractDao<T>{

    @PersistenceContext
    protected EntityManager entityManager;
    @Autowired
    protected CriteriaBuilder criteriaBuilder;

    /**
     * Pagination mechanism
     *
     */
    @Autowired
    private Paginator paginator;

    /**
     * find Entity by it's ID
     *
     * @param id Entity ID
     * @return Optional of Entity type
     */
    public abstract Optional<T> findById(long id);

    /**
     * add Entity to DB
     *
     * @param entity Entity
     * @return result of operation
     */
    public abstract T add(T entity);

    /**
     * delete exists Entity from DB
     *
     * @param id Entity id
     */
    public abstract boolean delete(long id);

    /**
     * find all Entities in DB
     *
     * @return List of Entities
     */
    public List<T> findAll(int currentPage, Class<T> entityClass) {
        CriteriaQuery<T> entityQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = entityQuery.from(entityClass);
        CriteriaQuery<T> findAllQuery = entityQuery.select(root);
        TypedQuery<T> typedQuery = entityManager.createQuery(findAllQuery);
        paginateQuery(currentPage, typedQuery);
        return typedQuery.getResultList();
    }

    /**
     * Protected method for query pagination
     *
     * @param currentPage Page number in URL
     * @param query Typed query for pagination
     */
    protected void paginateQuery(int currentPage, TypedQuery<?> query) {
        paginator.paginateQuery(currentPage, query);
    }
}
