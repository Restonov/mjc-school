package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Merging util using when current
 * Certificate are going to be updated
 *
 */
public class GiftCertificateUtils {

    private GiftCertificateUtils() {
    }

    public static void merge(GiftCertificate currentCertificate, GiftCertificate updatedCertificate) {
        if (updatedCertificate.getName() != null) {
            currentCertificate.setName(updatedCertificate.getName());
        }
        if (updatedCertificate.getDescription() != null) {
            currentCertificate.setDescription(updatedCertificate.getDescription());
        }
        if (updatedCertificate.getPrice() != null) {
            currentCertificate.setPrice(updatedCertificate.getPrice());
        }
        if (updatedCertificate.getDuration() != NumberUtils.INTEGER_ZERO) {
            currentCertificate.setDuration(updatedCertificate.getDuration());
        }
    }
}
