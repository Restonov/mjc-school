package com.epam.esm.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;

/**
 * Pagination mechanism
 *
 */
@Component
@PropertySource("classpath:pagination.properties")
public class Paginator {

    /**
     * Default value of results per page
     *
     */
    @Value("${pagination.results.per.page}")
    private int resultPerPage;

    private Paginator() {
    }

    public void paginateQuery(int currentPage, TypedQuery<?> typedQuery) {
        typedQuery.setFirstResult((currentPage - NumberUtils.INTEGER_ONE) * resultPerPage);
        typedQuery.setMaxResults(resultPerPage);
    }
}
