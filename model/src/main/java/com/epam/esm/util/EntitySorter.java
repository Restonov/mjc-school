package com.epam.esm.util;

import org.springframework.data.domain.Sort;

import static com.epam.esm.entity.Constants.NAME;
import static com.epam.esm.entity.Constants.CREATE_DATE;

/**
 * Sorter for findAllAndSort() service methods
 *
 */
public enum EntitySorter {
    NAME_ASC (Sort.by(NAME).ascending()),
    NAME_DESC (Sort.by(NAME).descending()),
    DATE_ASC (Sort.by(CREATE_DATE).ascending()),
    DATE_DESC (Sort.by(CREATE_DATE).descending());

    private final Sort sortType;

    EntitySorter(Sort sortType) {
        this.sortType = sortType;
    }

    public Sort getSortType() {
        return sortType;
    }
}
