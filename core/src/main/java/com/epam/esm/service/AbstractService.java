package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract service
 *
 * @param <T> the type parameter
 */
public abstract class AbstractService<T> {

    @Autowired
    protected GiftCertificateDao certificateDao;
    @Autowired
    protected GiftCertificateTagDao tagDao;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected UserOrderDao orderDao;
    @Autowired
    protected ObjectMapper objectMapper;

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
    public abstract List<T> findAll(int page);

    /**
     * Find resource by id
     *
     * @param id the id
     * @return optional resource
     */
    public abstract T findById(long id);

    /**
     * Delete resource by id
     *
     * @param id resource id
     */
    public abstract boolean delete(long id);
}

