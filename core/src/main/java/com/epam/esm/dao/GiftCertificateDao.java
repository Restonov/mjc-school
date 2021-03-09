package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;

import java.util.List;
import java.util.Set;

/**
 * Gift certificate dao
 */
public abstract class GiftCertificateDao extends AbstractDao<GiftCertificate> {

    /**
     * Find Certificates containing current tags
     *
     * @param page Page number in URL
     * @param tags current tags
     * @return Certificates
     */
    public abstract List<GiftCertificate> findByTags(int page, Set<GiftCertificateTag> tags);

    /**
     * Find certificates by tag id keyword
     *
     * @param keyWord cert name or description
     * @return certificates
     */
    public abstract List<GiftCertificate> findByKeyword(int page, String keyWord);

    /**
     *
     *
     * @param page Page number in URL
     * @param sort Sort type by name/description asc/desc
     * @return Certificates list
     */
    public abstract List<GiftCertificate> findAllAndSort(int page, String sort);
}
