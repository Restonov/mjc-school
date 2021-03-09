package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

/**
 * Dao layer main class
 * works with DB
 *
 */
public abstract class AbstractDao<T>{

    /**
     * find all Entities in DB
     *
     * @return List of Entities
     */
    public abstract List<T> findAll();

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
    public abstract void delete(long id);
}
