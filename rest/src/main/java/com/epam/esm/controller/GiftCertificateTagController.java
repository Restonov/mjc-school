package com.epam.esm.controller;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.impl.GiftCertificateTagServiceImpl;
import com.epam.esm.util.ResponseEntityWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Gift certificate Tag controller
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ParameterName.TAGS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateTagController {

    private final GiftCertificateTagServiceImpl service;

    /**
     * Create new Tag in DB
     *
     * @param tag new Certificate
     * @return ResponseEntity
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateTag> create(@RequestBody GiftCertificateTag tag) {
        GiftCertificateTag newTag = service.create(tag);
        return ResponseEntityWrapper.wrap(newTag);
    }

    /**
     * Find all Tags in DB
     *
     * @return Tags
     */
    @GetMapping()
    public List<GiftCertificateTag> findAll() {
        return service.findAll();
    }

    /**
     * Find Tag by id
     * if not found return 404 error
     *
     * @param id Tag id
     * @return Tag
     */
    @GetMapping("/{id}")
    public GiftCertificateTag findById(@PathVariable long id){
        GiftCertificateTag tag = null;
        Optional<GiftCertificateTag> optionalTag = service.findById(id);
        if (optionalTag.isPresent()) {
            tag = optionalTag.get();
        }
        return tag;
    }

    /**
     * Delete existing Tag
     *
     * @param id Tag id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
