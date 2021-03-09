package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.GiftCertificateUtils;
import com.epam.esm.util.QueryGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Gift certificate service implementation
 */
@NoArgsConstructor
@Transactional
@Service("giftCertificateService")
public class GiftCertificateServiceImpl extends GiftCertificateService {

    /**
     * Constructor for Test purposes
     *
     * @param certDao Certificate DAO
     * @param tagDao  Tag DAO
     */
    public GiftCertificateServiceImpl(GiftCertificateDao certDao, GiftCertificateTagDao tagDao) {
        this.certificateDao = certDao;
        this.tagDao = tagDao;
    }

    /**
     * We get Certificate dto without ID and any dates
     * but We get Tag collection and so
     * here implemented check:
     * if DB already contains Tag, we get it from DB (with exists ID)
     * and add it to our Certificate manually
     * else We just create new Tag and add it to Certificate
     * <p>
     * Unfortunately I can't find something that helps me with
     * automation this process by, for example, *Cascade* annotation param
     *
     * @param certificate Dto certificate
     * @return entity Certificate with ID & create / update Date
     */
    @Override
    public GiftCertificate create(@NotNull GiftCertificate certificate) {
        Set<GiftCertificateTag> tags = createCertificateTagRelation(certificate);
        certificate.setTags(tags);
        return certificateDao.add(certificate);
    }

    @Override
    public List<GiftCertificate> findAll(int currentPage) {
        return certificateDao.findAll(currentPage, GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> findAllAndSort(int page, String sort) {
        List<GiftCertificate> certificates;
        if (sort != null) {
            certificates = certificateDao.findAllAndSort(page, sort);
        } else {
            certificates = findAll(page);
        }
        return certificates;
    }

    @Override
    public GiftCertificate findById(long id) {
        GiftCertificate certificate;
        Optional<GiftCertificate> optionalCert = certificateDao.findById(id);
        if (optionalCert.isPresent()) {
            certificate = optionalCert.get();
        } else {
            throw new ResourceNotFoundException("certificate.not.found");
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByTagNames(int page, String... tagNames) {
        Set<GiftCertificateTag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tagName);
            if (optionalTag.isPresent()) {
                GiftCertificateTag tag = optionalTag.get();
                tags.add(tag);
            }
        }
        return certificateDao.findByTags(page, tags);
    }

    @Override
    public List<GiftCertificate> findByKeyWord(int page, String keyWord) {
        return certificateDao.findByKeyword(page, QueryGenerator.generateKeyWord(keyWord));
    }

    @Override
    @SneakyThrows
    public GiftCertificate update(long id, JsonPatch patch) {
        GiftCertificate currentCertificate;
        Optional<GiftCertificate> optionalCert = certificateDao.findById(id);
        if (optionalCert.isPresent()) {
            currentCertificate = optionalCert.get();
            JsonNode node = patch.apply(objectMapper.convertValue(currentCertificate, JsonNode.class));
            GiftCertificate patchedCertificate = objectMapper.treeToValue(node, GiftCertificate.class);
            GiftCertificateUtils.merge(currentCertificate, patchedCertificate);
            if (patchedCertificate.getTags() != null) {
                currentCertificate.setTags(patchedCertificate.getTags());
                currentCertificate.setTags(createCertificateTagRelation(currentCertificate));
            }
        } else {
            throw new ResourceNotFoundException("certificate.not.found");
        }
        return currentCertificate;
    }

    @Override
    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    /**
     * Manually create Certificate ~ Tag relation
     * for gift_certificate_tag table
     *
     * @param certificate Certificate
     * @return Tags set
     */
    private Set<GiftCertificateTag> createCertificateTagRelation(GiftCertificate certificate) {
        Set<GiftCertificateTag> currentDtoTags = certificate.getTags();
        Set<GiftCertificateTag> entityTags = new HashSet<>();
        for (GiftCertificateTag tag : currentDtoTags) {
            Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tag.getName());
            if (optionalTag.isPresent()) {
                GiftCertificateTag existingTag = optionalTag.get();
                existingTag.getCertificates().add(certificate);
                entityTags.add(existingTag);
            } else {
                GiftCertificateTag tagWithoutId = new GiftCertificateTag(tag.getName());
                Set<GiftCertificate> tagCertificates = new HashSet<>();
                tagCertificates.add(certificate);
                tagWithoutId.setCertificates(tagCertificates);
                GiftCertificateTag tagWithId = tagDao.add(tagWithoutId);
                entityTags.add(tagWithId);
            }
        }
        return entityTags;
    }
}
