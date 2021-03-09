package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.EntitySorter;
import com.epam.esm.util.GiftCertificateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Gift certificate service implementation
 */
@Transactional
@Service("giftCertificateService")
public class GiftCertificateServiceImpl extends GiftCertificateService {

    /**
     * Test constructor
     *
     * @param certDao Certificate dao
     * @param tagDao  Tag dao
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
        return certificateDao.save(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftCertificate> findAll(int currentPage, int pageSize) {
        Pageable pageAndResultPerPage = PageRequest.of(currentPage, pageSize);
        return certificateDao.findAll(pageAndResultPerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftCertificate> findAllAndSort(int currentPage, int pageSize, String sort) {
        Sort sortType;
        if (Arrays.stream(EntitySorter.values()).anyMatch(s -> s.name().equalsIgnoreCase(sort))) {
            sortType = EntitySorter.valueOf(sort.toUpperCase()).getSortType();
        } else {
            sortType = EntitySorter.NAME_ASC.getSortType();
        }
        Pageable pageSortedResult = PageRequest.of(currentPage, pageSize, sortType);
        return certificateDao.findAll(pageSortedResult);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Page<GiftCertificate> findByTagNames(int currentPage, int pageSize, String... tagNames) {
        Set<GiftCertificateTag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tagName);
            if (optionalTag.isPresent()) {
                GiftCertificateTag tag = optionalTag.get();
                tags.add(tag);
            }
        }
        Pageable pageWithResult = PageRequest.of(currentPage, pageSize);
        return certificateDao.findByTagsIn(tags, pageWithResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftCertificate> findByKeyWord(int currentPage, int pageSize, String keyWord) {
        Pageable pageWithResult = PageRequest.of(currentPage, pageSize);
        return certificateDao
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyWord, keyWord, pageWithResult);
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
    public void delete(long id) {
        Optional<GiftCertificate> optionalCertificate = certificateDao.findById(id);
        optionalCertificate.ifPresent(c -> certificateDao.delete(c));
    }

    /**
     * Manually create Certificate ~ Tag relation
     * for gift_certificate_tag table
     *
     * @param certificate Certificate
     * @return Tags set
     */
    public Set<GiftCertificateTag> createCertificateTagRelation(GiftCertificate certificate) {
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
                GiftCertificateTag tagWithId = tagDao.save(tagWithoutId);
                entityTags.add(tagWithId);
            }
        }
        return entityTags;
    }
}
