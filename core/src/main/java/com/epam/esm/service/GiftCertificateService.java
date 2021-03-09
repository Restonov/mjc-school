package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

/**
 * Gift certificate service.
 */
public abstract class GiftCertificateService extends AbstractService<GiftCertificate> {

    /**
     * Find certificates by keyWord
     *
     * @param keyWord certificate name or description
     * @return certificates, containing keyword
     */
    public abstract List<GiftCertificate> findByKeyWord(int page, String keyWord);

    /**
     * Find all certificates with received Tag
     *
     * @param tagNames name of the Tags
     * @return certificates
     */
    public abstract List<GiftCertificate> findByTagNames(int page, String... tagNames);

    /**
     * Find all Certificates in sorted order
     *
     * @param page page in URL
     * @param sort name/description asc/desc
     * @return Certificates
     */
    public abstract List<GiftCertificate> findAllAndSort(int page, String sort);

    /**
     * Update certificate
     *
     * @param patch JsonPatch patch
     * @param id    Certificate id
     */
    public abstract GiftCertificate update(long id, JsonPatch patch);
}
