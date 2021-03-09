package com.epam.esm.controller;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;

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
 * Gift certificate controller
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ParameterName.CERTIFICATES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService service;

    /**
     * Create new Certificate in DB
     *
     * @param certificate new Certificate
     * @return ResponseEntity
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate certificate) {
        GiftCertificate newCert = service.create(certificate);
        return ResponseEntityWrapper.wrap(newCert);
    }

    /**
     * Find all Certificates in DB
     *
     * @return Certificate
     */
    @GetMapping()
    public List<GiftCertificate> findAll() {
        return service.findAll();
    }

    /**
     * Find all Certificates and return it in sorted state
     *
     * @param sort    name / create date
     * @param order   asc / desc
     * @return the list
     */
    @GetMapping(params = {"sort", "order"})
    public List<GiftCertificate> findAllAndSort(@RequestParam String sort, @RequestParam String order) {
        return service.findAllAndSort(sort, order);
    }

    /**
     * Find Certificate by id
     * if not found return 404 error
     *
     * @param id Certificate id
     * @return Certificate
     */
    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable long id) {
        GiftCertificate certificate = null;
        Optional<GiftCertificate> optionalCert = service.findById(id);
        if (optionalCert.isPresent()) {
            certificate = optionalCert.get();
        }
        return certificate;
    }

    /**
     * Find Certificate by tag name
     *
     * @param tagName name of the Tag
     * @return Certificates contains this Tag
     */
    @GetMapping(params = {"tagName"})
    public List<GiftCertificate> findByTagName(@RequestParam String tagName) {
        return service.findByTagName(tagName);
    }

    /**
     * Find Certificate by keyWord
     *
     * @param search name / description
     * @return Certificates contains this keyWord
     */
    @GetMapping(params = {"search"})
    public List<GiftCertificate> findByKeyWord(@RequestParam String search) {
        return service.findByKeyWord(search);
    }

    /**
     * Update existing Certificate
     *
     * @param certificate Certificate
     * @param id          Certificate id
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody GiftCertificate certificate, @PathVariable long id) {
        service.update(certificate, id);
    }

    /**
     * Delete existing Certificate
     *
     * @param id Certificate id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
