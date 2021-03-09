package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.ParameterName;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.ResponseEntityWrapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Gift certificate controller
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ParameterName.CERTIFICATES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController{

    private final GiftCertificateService service;

    /**
     * Create new Certificate in DB
     *
     * @param certificateDto new Certificate
     * @return ResponseEntity<Certificate>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate certificateDto) {
        GiftCertificate certificate = service.create(certificateDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ParameterName.CERTIFICATES_URL + "/{id}")
                .buildAndExpand(certificateDto.getId()).toUri();
        return ResponseEntity.created(uri).body(certificate);
    }

    /**
     * Find all Certificates
     *
     * @param page pagination page
     * @return List of Certificates
     */
    @GetMapping(params = {"page"})
    public ResponseEntity<List<GiftCertificate>> findAll( @RequestParam(defaultValue = "1") @Min(1) int page ) {
        List<GiftCertificate> certificates = service.findAll(page);
        return new ResponseEntity<>(certificates, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Find all Certificates
     *
     * @param page pagination page
     * @param sort name_asc/desc or date_asc/desc
     * @return List of Certificates
     */
    @GetMapping(params = {"page", "sort"})
    public ResponseEntity<List<GiftCertificate>> findAllAndSort( @RequestParam(defaultValue = "1") @Min(1) int page,
                                                          @RequestParam String sort) {
        List<GiftCertificate> certificates = service.findAllAndSort(page, sort);
        return new ResponseEntity<>(certificates, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Find Certificate by id
     * if not found return 404 error
     *
     * @param id Certificate id
     * @return Certificate
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> findById(@PathVariable @Min(1) long id) {
        GiftCertificate certificate = service.findById(id);
        return EntityModel.of(certificate,
                linkTo(methodOn(GiftCertificateController.class)
                        .findById(id))
                        .withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .create(certificate))
                        .withRel(ParameterName.CREATE_CERTIFICATE)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .findByTagName(NumberUtils.INTEGER_ONE, new String[]{ParameterName.TAG_NAME}))
                        .withRel(ParameterName.BY_TAG_NAME),
                linkTo(methodOn(GiftCertificateController.class)
                        .findByKeyWord(ParameterName.KEY_WORD, NumberUtils.INTEGER_ONE))
                        .withRel(ParameterName.BY_KEYWORD),
                linkTo(methodOn(GiftCertificateController.class)
                        .findAll(NumberUtils.INTEGER_ONE))
                        .withRel(ParameterName.ALL_CERTIFICATES),
                linkTo(methodOn(GiftCertificateController.class)
                        .findAllAndSort(NumberUtils.INTEGER_ONE, ParameterName.SORT_BY))
                        .withRel(ParameterName.ALL_SORTED_CERTIFICATES),
                linkTo(methodOn(GiftCertificateController.class)
                        .delete(id)).withRel(ParameterName.DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name())
        );
    }

    /**
     * Find Certificate by tag name
     *
     * @param tags name of the Tag
     * @return Certificates contains this Tag
     */
    @GetMapping(params = {"page", "tags"})
    public ResponseEntity<List<GiftCertificate>> findByTagName(@RequestParam(defaultValue = "1") @Min(1) int page,
                                                               @RequestParam String[] tags) {
        List<GiftCertificate> certificates = service.findByTagNames(page, tags);
        return new ResponseEntity<>(certificates, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Find all Certificates
     *
     * @param search name/description
     * @param page pagination page
     * @return List of Certificates
     */
    @GetMapping(params = {"search", "page"})
    public ResponseEntity<List<GiftCertificate>> findByKeyWord(@RequestParam String search,
                                                               @RequestParam(defaultValue = "1") @Min(1) int page) {
        List<GiftCertificate> certificates = service.findByKeyWord(page, search);
        return new ResponseEntity<>(certificates, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Update each field of Certificates via patch
     *
     * @param id Certificate id
     * @param patch Json patch
     * @return ResponseEntity<Certificate>
     */
    @PatchMapping(path = "/{id}", consumes = ParameterName.APPLICATION_JSON_PATCH)
    public ResponseEntity<GiftCertificate> update(@PathVariable @Min(1) long id, @RequestBody JsonPatch patch) {
        GiftCertificate certificate = service.update(id, patch);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    /**
     * Delete existing Certificate
     *
     * @param id Certificate id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable @Min(1) long id) {
        boolean operationResult = service.delete(id);
        return ResponseEntityWrapper.wrapBoolean(operationResult);
    }
}
