package com.epam.esm.controller;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.util.ResponseEntityWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Gift certificate Tag controller
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ParameterName.TAGS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateTagController {

    private final GiftCertificateTagService service;

    /**
     * Create new Tag in DB
     *
     * @param tagDto new Tag
     * @return ResponseEntity<Tag>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateTag> create(@Valid @RequestBody GiftCertificateTag tagDto) {
        GiftCertificateTag tag = service.create(tagDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ParameterName.USERS_URL + "/{id}")
                .buildAndExpand(tag.getId()).toUri();
        return ResponseEntity.created(uri).body(tag);
    }

    /**
     * Find all Tags
     *
     * @param page pagination page
     * @return List of Tags
     */
    @GetMapping(params = {"page"})
    public ResponseEntity<List<GiftCertificateTag>> findAll(@RequestParam(defaultValue = "1") @Min(1) int page) {
        List<GiftCertificateTag> tags = service.findAll(page);
        return new ResponseEntity<>(tags, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Find most profitable Tag
     *
     * @return ResponseEntity<GiftCertificateTag>
     */
    @GetMapping("/profit")
    public ResponseEntity<GiftCertificateTag> findMostProfitable() {
        GiftCertificateTag tag = service.findMostProfitableTag();
        return new ResponseEntity<>(tag, HttpStatus.FOUND);
    }

    /**
     * Find Tag by id
     * if not found return 404 error
     *
     * @param tagId Tag id
     * @return Tag
     */
    @GetMapping("/{tagId}")
    public EntityModel<GiftCertificateTag> findById(@PathVariable @Min(1) long tagId) {
        GiftCertificateTag tag = service.findById(tagId);
        return EntityModel.of(tag,
                linkTo(methodOn(GiftCertificateTagController.class)
                        .findById(tagId)).withSelfRel(),
                linkTo(methodOn(GiftCertificateTagController.class)
                        .create(tag))
                        .withRel(ParameterName.CREATE_TAG).withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateTagController.class).findAll(1))
                        .withRel(ParameterName.ALL_TAGS),
                linkTo(methodOn(GiftCertificateTagController.class).findMostProfitable())
                        .withRel(ParameterName.MOST_PROFITABLE_TAG),
                linkTo(methodOn(GiftCertificateTagController.class)
                        .delete(tagId)).withRel(ParameterName.DELETE_TAG).withType(HttpMethod.DELETE.name())
        );
    }
    /**
     * Delete existing Tag
     *
     * @param id Tag id
     * @return ResponseEntity<Tag>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable @Min(1) long id) {
        boolean operationResult = service.delete(id);
        return ResponseEntityWrapper.wrapBoolean(operationResult);
    }
}
