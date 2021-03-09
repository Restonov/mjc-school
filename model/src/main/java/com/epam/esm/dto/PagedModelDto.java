package com.epam.esm.dto;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PagedModelDto extends ResponseEntity<PagedModel<?>> {

    public PagedModelDto(PagedModel<?> body, HttpStatus status) {
        super(body, status);
    }
}
