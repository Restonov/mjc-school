package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Gift certificate Tag resource
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateTag {
    private long tagId;
    private String name;

    public GiftCertificateTag(String name) {
        this.name = name;
    }
}
