package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateTagDto;
import com.epam.esm.dto.PagedModelDto;
import com.epam.esm.entity.Constants;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.util.DtoConverter;
import com.epam.esm.util.EntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Gift certificate Tag controller
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.TAGS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateTagController {

    private final GiftCertificateTagService service;

    /**
     * Create new Tag in DB
     *
     * @param tagDto new Tag
     * @return ResponseEntity<Tag>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateTagDto> create(@RequestBody @Valid GiftCertificateTagDto tagDto) {
        GiftCertificateTag tag = service.create(DtoConverter.convertTagToEntity(tagDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(Constants.USERS_URL + "/{id}")
                .buildAndExpand(tag.getId()).toUri();
        return ResponseEntity
                .created(uri)
                .body(EntityConverter.convertTagToDto(tag));
    }

    /**
     * Find all Tags
     *
     * @param page pagination page
     * @return List of Tags
     */
    @GetMapping(params = {"page"})
    public PagedModelDto findAll(@RequestParam @Min(0) int page,
                                 @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                 PagedResourcesAssembler<GiftCertificateTag> assembler) {
        Page<GiftCertificateTag> tags = service.findAll(page, size);
        return new PagedModelDto(
                assembler.toModel(tags),
                HttpStatus.FOUND
        );
    }

    /**
     * Find most profitable Tag
     *
     * @return ResponseEntity<GiftCertificateTag>
     */
    @GetMapping("/profit")
    public ResponseEntity<GiftCertificateTagDto> findMostProfitable() {
        GiftCertificateTag tag = service.findMostProfitableTag();
        return new ResponseEntity<>(
                EntityConverter.convertTagToDto(tag),
                HttpStatus.FOUND
        );
    }

    /**
     * Find Tag by id
     * if not found return 404 error
     *
     * @param tagId Tag id
     * @return Tag
     */
    @GetMapping("/{tagId}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<GiftCertificateTagDto> findById(@PathVariable @Min(1) long tagId) {
        GiftCertificateTag tag = service.findById(tagId);
        return EntityModel.of(
                EntityConverter.convertTagToDto(tag),
                linkTo(methodOn(GiftCertificateTagController.class)
                        .findById(tagId)).withSelfRel(),
                linkTo(methodOn(GiftCertificateTagController.class)
                        .create(new GiftCertificateTagDto()))
                        .withRel(Constants.CREATE_TAG).withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateTagController.class).findMostProfitable())
                        .withRel(Constants.MOST_PROFITABLE_TAG),
                linkTo(methodOn(GiftCertificateTagController.class)
                        .delete(tagId)).withRel(Constants.DELETE_TAG).withType(HttpMethod.DELETE.name())
        );
    }

    /**
     * Delete existing Tag
     *
     * @param id Tag id
     * @return ResponseEntity<Tag>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) long id) {
        service.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
