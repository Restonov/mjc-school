package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Constants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.DtoConverter;
import com.epam.esm.util.EntityConverter;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Gift certificate controller
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.CERTIFICATES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService service;

    /**
     * Create new Certificate in DB
     *
     * @param certificateDto new Certificate
     * @return ResponseEntity<Certificate>
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificateDto) {
        GiftCertificate certificate = service.create(DtoConverter.convertCertificateToEntity(certificateDto));
        return EntityModel.of(
                EntityConverter.convertCertificateToDto(certificate),
                linkTo(methodOn(GiftCertificateController.class)
                        .findById(certificate.getId()))
                        .withSelfRel()
        );
    }

    /**
     * Find all Certificates
     *
     * @param page pagination page
     * @param size results per page
     * @return List of Certificates
     */
    @GetMapping()
    public ResponseEntity<PagedModel<?>> findAll(@RequestParam(required = false, defaultValue = "0") @Min(0) int page,
                                                 @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                 PagedResourcesAssembler<GiftCertificate> assembler) {
        Page<GiftCertificate> certificates = service.findAll(page, size);
        return new ResponseEntity<>(
                assembler.toModel(certificates),
                HttpStatus.FOUND
        );
    }

    /**
     * Find all Certificates
     *
     * @param page pagination page
     * @param sort name_asc/desc or date_asc/desc
     * @return List of Certificates
     */
    @GetMapping(params = {"page", "sort"})
    public ResponseEntity<PagedModel<?>> findAllAndSort(@RequestParam @Min(0) int page,
                                                        @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                        @RequestParam String sort,
                                                        PagedResourcesAssembler<GiftCertificate> assembler) {
        Page<GiftCertificate> certificates = service.findAllAndSort(page, size, sort);
        return new ResponseEntity<>(
                assembler.toModel(certificates),
                HttpStatus.FOUND
        );
    }

    /**
     * Find Certificate by id
     * if not found return 404 error
     *
     * @param id Certificate id
     * @return Certificate
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<GiftCertificateDto> findById(@PathVariable @Min(1) long id) {
        GiftCertificate certificate = service.findById(id);
        return EntityModel.of(
                EntityConverter.convertCertificateToDto(certificate),
                linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificateDto()))
                        .withRel(Constants.CREATE_CERTIFICATE)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .delete(id)).withRel(Constants.DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .findAll(0, 10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(Constants.ALL_CERTIFICATES)
        );
    }

    /**
     * Find Certificate by tag name
     *
     * @param tags name of the Tag
     * @return Certificates contains this Tag
     */
    @GetMapping(params = {"page", "tags"})
    public ResponseEntity<PagedModel<?>> findByTagName(@RequestParam @Min(0) int page,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                       @RequestParam String[] tags,
                                                       PagedResourcesAssembler<GiftCertificate> assembler) {
        Page<GiftCertificate> certificates = service.findByTagNames(page, size, tags);
        return new ResponseEntity<>(
                assembler.toModel(certificates),
                HttpStatus.FOUND
        );
    }

    /**
     * Find all Certificates
     *
     * @param search name/description
     * @param page   pagination page
     * @return List of Certificates
     */
    @GetMapping(params = {"search", "page"})
    public ResponseEntity<PagedModel<?>> findByKeyWord(@RequestParam String search,
                                                       @RequestParam @Min(0) int page,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                       PagedResourcesAssembler<GiftCertificate> assembler) {
        Page<GiftCertificate> certificates = service.findByKeyWord(page, size, search);
        return new ResponseEntity<>(
                assembler.toModel(certificates),
                HttpStatus.FOUND
        );
    }

    /**
     * Update each field of Certificates via patch
     *
     * @param id    Certificate id
     * @param patch Json patch
     * @return ResponseEntity<Certificate>
     */
    @PatchMapping(path = "/{id}", consumes = Constants.APPLICATION_JSON_PATCH)
    public ResponseEntity<GiftCertificateDto> update(@PathVariable @Min(1) long id, @RequestBody JsonPatch patch) {
        GiftCertificate certificate = service.update(id, patch);
        return new ResponseEntity<>(
                EntityConverter.convertCertificateToDto(certificate),
                HttpStatus.OK
        );
    }

    /**
     * Delete existing Certificate
     *
     * @param id Certificate id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) long id) {
        service.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
