package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificateTag;

import java.util.Optional;

/**
 * Gift certificate tag dao
 */
public abstract class GiftCertificateTagDao extends AbstractDao<GiftCertificateTag> {

    /**
     * Find Tag by name
     *
     * @param tagName Tag name
     * @return optional Tag
     */
    public abstract Optional<GiftCertificateTag> findByName(String tagName);
}
