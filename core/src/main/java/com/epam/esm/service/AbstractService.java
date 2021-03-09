package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

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

    /**
     * Create resource
     *
     * @param resource the entity
     * @return the t
     */
    public abstract T create(T resource);

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
    public abstract void delete(long id);

    /**
     * Find all resources
     *
     * @return resource
     */
    public abstract Page<T> findAll(int pageNum, int pageSize);
}

