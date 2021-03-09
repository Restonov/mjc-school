package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificateTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Gift certificate dao
 */
public interface GiftCertificateTagDao extends JpaRepository<GiftCertificateTag, Long> {

    /**
     * Find Tag by Name
     *
     * @param tagName Tag name
     * @return Optional of Tag
     */
    Optional<GiftCertificateTag> findByName(String tagName);
}
