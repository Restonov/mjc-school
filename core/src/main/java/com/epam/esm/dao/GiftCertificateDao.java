package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Gift certificate dao
 */
public interface GiftCertificateDao extends JpaRepository<GiftCertificate, Long> {

    /**
     * Find Certificates that contains provided Set of Tags
     *
     * @param tags     Tags
     * @param pageable Num and size of results
     * @return Page of Certificates
     */
    Page<GiftCertificate> findByTagsIn(Set<GiftCertificateTag> tags, Pageable pageable);

    /**
     * Find Certificates that contains provided name or description
     *
     * @param name        Certificate name
     * @param description Certificate description
     * @param pageable    Num and size of results
     * @return Page of Certificates
     */
    Page<GiftCertificate> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name,
                                                                                          String description,
                                                                                          Pageable pageable);
}
