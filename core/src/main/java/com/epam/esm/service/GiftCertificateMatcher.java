package com.epam.esm.service;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificate;

import java.time.LocalDateTime;


/**
 * Gift certificate matcher
 */
public class GiftCertificateMatcher {

    private GiftCertificateMatcher() {
    }

    /**
     * Match old certificate with new one
     * replace all new parameters
     *
     * @param oldCert the old cert
     * @param newCert the new cert
     * @return the gift certificate
     */
    public static GiftCertificate match(GiftCertificate oldCert, GiftCertificate newCert) {
        if (newCert.getName() == null) {
            newCert.setName(oldCert.getName());
        }
        if (newCert.getDescription() == null) {
            newCert.setDescription(oldCert.getDescription());
        }
        if (newCert.getDuration() == ParameterName.ZERO) {
            newCert.setDuration(oldCert.getDuration());
        }
        if (newCert.getPrice() == null) {
            newCert.setPrice(oldCert.getPrice());
        }
        newCert.setCertificateId(oldCert.getCertificateId());
        newCert.setLastUpdateDate(LocalDateTime.now());
        return newCert;
    }
}
