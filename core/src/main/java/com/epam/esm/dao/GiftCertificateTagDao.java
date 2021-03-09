package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificateTag;

import java.util.List;
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

    /**
     * Find Tags by Certificate id
     *
     * @param certificateId Certificate id
     * @return tags
     */
    public abstract List<GiftCertificateTag> findByCertificateId(long certificateId);

}
