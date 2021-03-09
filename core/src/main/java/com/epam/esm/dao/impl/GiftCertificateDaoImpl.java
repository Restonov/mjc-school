package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificate;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Gift certificate dao implementation
 */
@Repository("giftCertificateDao")
public class GiftCertificateDaoImpl extends GiftCertificateDao {
    private static final String SELECT_ALL_CERTIFICATES = "SELECT c FROM GiftCertificate c ";
    private static final String ORDER_BY_NAME_ASC = "ORDER BY c.name ASC";
    private static final String ORDER_BY_NAME_DESC = "ORDER BY c.name DESC";
    private static final String ORDER_BY_DATE_ASC = "ORDER BY c.createDate ASC";
    private static final String ORDER_BY_DATE_DESC = "ORDER BY c.createDate DESC";

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public List<GiftCertificate> findAllAndSort(int page, String sortBy) {
        StringBuilder hql = new StringBuilder(SELECT_ALL_CERTIFICATES);
        switch (sortBy) {
            case ParameterName.NAME_DESC:
                hql.append(ORDER_BY_NAME_DESC);
                break;
            case ParameterName.DATE_ASC:
                hql.append(ORDER_BY_DATE_ASC);
                break;
            case ParameterName.DATE_DESC:
                hql.append(ORDER_BY_DATE_DESC);
                break;
            default:
                hql.append(ORDER_BY_NAME_ASC);
        }
        TypedQuery<GiftCertificate> typedQuery = entityManager
                .createQuery(hql.toString(), GiftCertificate.class);

        paginateQuery(page, typedQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(GiftCertificate.class, id)
        );
    }

    @Override
    @SneakyThrows
    public List<GiftCertificate> findByTags(int page, Set<GiftCertificateTag> tags) {
        CriteriaQuery<GiftCertificate> findQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = findQuery.from(GiftCertificate.class);

        Predicate[] allTags = new Predicate[tags.size()];
        int tagCount = NumberUtils.INTEGER_ZERO;
        for (GiftCertificateTag tag : tags) {
            Predicate containTag = criteriaBuilder.isMember(tag, root.get(ParameterName.TAGS));
            allTags[tagCount] = containTag;
            tagCount++;
        }
        Predicate containAllTags = criteriaBuilder.and(allTags);
        findQuery.select(root).where(containAllTags);
        TypedQuery<GiftCertificate> findCertificatesQuery = entityManager.createQuery(findQuery);

        paginateQuery(page, findCertificatesQuery);
        return findCertificatesQuery.getResultList();
    }

    @Override
    public List<GiftCertificate> findByKeyword(int page, String keyWord) {
        CriteriaQuery<GiftCertificate> findQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = findQuery.from(GiftCertificate.class);

        Predicate descriptionLikeKeyWord = criteriaBuilder.like(root.get(ParameterName.DESCRIPTION), keyWord);
        Predicate nameLikeKeyWord = criteriaBuilder.like(root.get(ParameterName.NAME), keyWord);
        Predicate nameOrDescription = criteriaBuilder.or(descriptionLikeKeyWord, nameLikeKeyWord);

        findQuery.select(root).where(nameOrDescription);
        TypedQuery<GiftCertificate> findCertificatesQuery = entityManager.createQuery(findQuery);

        paginateQuery(page, findCertificatesQuery);
        return findCertificatesQuery.getResultList();
    }

    @Override
    public boolean delete(long id) {
        boolean result = false;
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
        if (certificate != null) {
            entityManager.remove(certificate);
            result = true;
        }
        return result;
    }
}
