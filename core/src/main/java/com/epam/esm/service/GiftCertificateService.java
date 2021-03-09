package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Gift certificate service.
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class GiftCertificateService extends AbstractService<GiftCertificate> {

    @Autowired protected GiftCertificateDao certDao;
    @Autowired protected GiftCertificateTagDao tagDao;

    /**
     * Find certificates by keyWord
     *
     * @param keyWord certificate name or description
     * @return certificates, containing keyword
     */
    public abstract List<GiftCertificate> findByKeyWord(String keyWord);

    /**
     * Find all certificates and sort with comparator
     *
     * @param sortBy name or creation date
     * @param orderBy ascending or descending
     * @return sorted certificates
     */
    public abstract List<GiftCertificate> findAllAndSort(String sortBy, String orderBy);

    /**
     * Find all certificates with received Tag
     *
     * @param tagName name of the Tag
     * @return certificates
     */
    public abstract List<GiftCertificate> findByTagName(String tagName);

    /**
     * Update certificate
     *
     * @param certificate Certificate
     * @param id Certificate id
     */
    public abstract void update(GiftCertificate certificate, long id);
}
