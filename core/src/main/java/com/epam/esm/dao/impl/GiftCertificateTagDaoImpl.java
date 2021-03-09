package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificateTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * Gift certificate tag dao implementation
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class GiftCertificateTagDaoImpl extends GiftCertificateTagDao {
    private static final String SELECT_TAG_BY_TAG_NAME = "SELECT t FROM GiftCertificateTag t WHERE t.name = :name";

    @Override
    public GiftCertificateTag add(GiftCertificateTag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Optional<GiftCertificateTag> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(GiftCertificateTag.class, id)
        );
    }

    @Override
    public Optional<GiftCertificateTag> findByName(String tagName) {
        Optional<GiftCertificateTag> optionalTag;
        try {
            optionalTag = Optional.ofNullable(entityManager.createQuery(SELECT_TAG_BY_TAG_NAME, GiftCertificateTag.class).
                    setParameter(ParameterName.NAME, tagName).getSingleResult()
            );
        } catch (NoResultException e) {
            optionalTag = Optional.empty();
        }
        return optionalTag;
    }

    @Override
    public boolean delete(long id) {
        boolean result = false;
        GiftCertificateTag tag = entityManager.find(GiftCertificateTag.class, id);
        if (tag != null) {
            entityManager.remove(tag);
            result = true;
        }
        return result;
    }
}
