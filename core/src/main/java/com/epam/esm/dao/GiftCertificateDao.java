package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

/**
 * Gift certificate dao
 */
public abstract class GiftCertificateDao extends AbstractDao<GiftCertificate> {

    /**
     * Find certificates by tag id
     *
     * @param tagId tag id
     * @return certificates
     */
    public abstract List<GiftCertificate> findByTagId(long tagId);

    /**
     * Find certificates by tag id keyword
     *
     * @param keyWord cert name or description
     * @return certificates
     */
    public abstract List<GiftCertificate> findByKeyword(String keyWord);

    /**
     * Update certificate
     *
     * @param certificate certificate
     */
    public abstract void update(GiftCertificate certificate);

    /**
     * Add certificate-tag relation
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     */
    public abstract void addRelation(long certificateId, long tagId);

    /**
     * Find certificate-tag relation
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return result
     */
    public abstract boolean findRelation(long certificateId, long tagId);
}
