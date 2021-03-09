package com.epam.esm.comparator;

import com.epam.esm.entity.GiftCertificate;

import java.util.Comparator;

/**
 * Gift certificate comparator
 * using in findAllAndLSort service method
 */
public enum GiftCertificateComparator implements Comparator<GiftCertificate> {

    DATE {
        @Override
        public int compare(GiftCertificate cert1, GiftCertificate cert2) {
            return cert1.getCreateDate().compareTo(cert2.getCreateDate());
        }
    },
    NAME {
        @Override
        public int compare (GiftCertificate cert1, GiftCertificate cert2){
            return cert1.getName().compareTo(cert2.getName());
        }
    }
}
