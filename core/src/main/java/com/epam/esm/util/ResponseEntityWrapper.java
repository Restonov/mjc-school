package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.ParameterName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Wrap resources into ResponseEntity
 * and create URI for it
 */
public class ResponseEntityWrapper {

    private ResponseEntityWrapper() {
    }

    public static ResponseEntity<GiftCertificate> wrap(GiftCertificate certificate) {
        ResponseEntity<GiftCertificate> responseEntity;
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ParameterName.CERTIFICATES_URL + "/{id}")
                .buildAndExpand(certificate.getCertificateId()).toUri();
        responseEntity = ResponseEntity.created(uri).body(certificate);
        return responseEntity;
    }

    public static ResponseEntity<GiftCertificateTag> wrap(GiftCertificateTag tag) {
        ResponseEntity<GiftCertificateTag> responseEntity;
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ParameterName.TAGS_URL + "/{id}")
                .buildAndExpand(tag.getTagId()).toUri();
        responseEntity = ResponseEntity.created(uri).body(tag);
        return responseEntity;
    }
}
