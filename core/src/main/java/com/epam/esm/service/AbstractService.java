package com.epam.esm.service;

import java.util.List;
import java.util.Optional;


/**
 * Abstract service
 *
 * @param <T> the type parameter
 */
public abstract class AbstractService<T> {

    /**
     * Create resource
     *
     * @param resource the entity
     * @return the t
     */
    public abstract T create(T resource);

    /**
     * Find all resources
     *
     * @return resource
     */
    public abstract List<T> findAll();

    /**
     * Find resource by id
     *
     * @param id the id
     * @return optional resource
     */
    public abstract Optional<T> findById(long id);

    /**
     * Delete resource by id
     *
     * @param id resource id
     */
    public abstract void delete(long id);
}

