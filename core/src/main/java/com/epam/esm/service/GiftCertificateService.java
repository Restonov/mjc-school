package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Gift certificate service.
 */
public abstract class GiftCertificateService extends AbstractService<GiftCertificate> {

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Find certificates by keyWord
     *
     * @param keyWord certificate name or description
     * @return certificates, containing keyword
     */
    public abstract Page<GiftCertificate> findByKeyWord(int page, int pageSize, String keyWord);

    /**
     * Find all certificates with received Tag
     *
     * @param tagNames name of the Tags
     * @return certificates
     */
    public abstract Page<GiftCertificate> findByTagNames(int page, int pageSize, String... tagNames);

    /**
     * Find all Certificates in sorted order
     *
     * @param pageNum page in URL
     * @param pageSize page size
     * @param sortType name/description asc/desc
     * @return Certificates
     */
    public abstract Page<GiftCertificate> findAllAndSort(int pageNum, int pageSize, String sortType);

    /**
     * Update certificate
     *
     * @param patch JsonPatch patch
     * @param id    Certificate id
     */
    public abstract GiftCertificate update(long id, JsonPatch patch);
}
