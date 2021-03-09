package com.epam.esm.service.impl;

import com.epam.esm.comparator.GiftCertificateComparator;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateMatcher;
import com.epam.esm.util.QueryGenerator;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Gift certificate service implementation
 */
@NoArgsConstructor
@Transactional
@Service("giftCertificateService")
public class GiftCertificateServiceImpl extends GiftCertificateService{

    /**
     * Instantiates a new Gift certificate service
     *
     * @param certDao cert dao
     * @param tagDao  tag dao
     */
    public GiftCertificateServiceImpl(GiftCertificateDao certDao, GiftCertificateTagDao tagDao) {
        super(certDao, tagDao);
    }

    @Override
    public GiftCertificate create(@NotNull GiftCertificate certificate){
            LocalDateTime currentTime = LocalDateTime.now();
            certificate.setCreateDate(currentTime);
            certificate.setLastUpdateDate(currentTime);
            GiftCertificate createdCertificate = certDao.add(certificate);
            createCertificateTagRelation(createdCertificate);
        return createdCertificate;
    }

    @Override
    public List<GiftCertificate> findAll(){
        List<GiftCertificate> certificates = certDao.findAll();
            for (GiftCertificate certificate : certificates) {
                List<GiftCertificateTag> tags = tagDao.findByCertificateId(certificate.getCertificateId());
                List<String> tagNames = tags.stream().map(GiftCertificateTag::getName).collect(Collectors.toList());
                certificate.setTags(tagNames);
            }
        return certificates;
    }

    public List<GiftCertificate> findAllAndSort(String sortBy, String orderType){
        List<GiftCertificate> certificates = findAll();
        boolean comparatorExists = Arrays.stream(GiftCertificateComparator.values())
                    .anyMatch(c -> c.name().equalsIgnoreCase(sortBy));
        Comparator<GiftCertificate> comparator = comparatorExists
                ? GiftCertificateComparator.valueOf(sortBy.toUpperCase())
                : GiftCertificateComparator.NAME;
            if (orderType.equalsIgnoreCase(ParameterName.DESCENDING)) {
                certificates.sort(comparator.reversed());
            } else {
                certificates.sort(comparator);
            }
        return certificates;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        Optional<GiftCertificate> certificate;
        try {
            certificate = certDao.findById(id);
            if (certificate.isPresent()) {
                GiftCertificate certificateWithTags = certificate.get();
                List<GiftCertificateTag> tags = tagDao.findByCertificateId(id);
                List<String> tagNames = tags.stream().map(GiftCertificateTag::getName).collect(Collectors.toList());
                certificateWithTags.setTags(tagNames);
            }
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Requested resource not found (id = " + id + ")", e);
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) {
        List<GiftCertificate> certificates = new ArrayList<>();
            Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tagName);
            if (optionalTag.isPresent()) {
                GiftCertificateTag tag = optionalTag.get();
                certificates = certDao.findByTagId(tag.getTagId());
                List<String> tagNames = Stream.of(tagName).collect(Collectors.toList());
                certificates.forEach(c -> c.setTags(tagNames));
            }
        return certificates;
    }

    @Override
    public List<GiftCertificate> findByKeyWord(String keyWord) {
        List<GiftCertificate> certificates;
            certificates = certDao.findByKeyword(QueryGenerator.generateKeyWord(keyWord));
            if (certificates != null) {
                for (GiftCertificate certificate : certificates) {
                    List<GiftCertificateTag> tags = tagDao.findByCertificateId(certificate.getCertificateId());
                    List<String> tagNames = tags.stream().map(GiftCertificateTag::getName).collect(Collectors.toList());
                    certificate.setTags(tagNames);
                }
            }
        return certificates;
    }

    @Override
    public void update(GiftCertificate newCert, long id) {
        try {
            Optional<GiftCertificate> optionalCertificate = certDao.findById(id);
            if (optionalCertificate.isPresent()) {
                GiftCertificate oldCert = optionalCertificate.get();
                GiftCertificate certificate = GiftCertificateMatcher.match(oldCert, newCert);
                certDao.update(certificate);
                createCertificateTagRelation(certificate);
            }
        } catch (DataAccessException ignored) { }
    }

    @Override
    public void delete(long id){
        certDao.delete(id);
    }

    private void createCertificateTagRelation(GiftCertificate certificate) {
        List<String> tagNames = certificate.getTags();
        if (tagNames != null) {
            long certId = certificate.getCertificateId();
            for (String tagName : tagNames) {
                Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tagName);
                if (optionalTag.isPresent()) {
                    if (!certDao.findRelation(certId, optionalTag.get().getTagId())){
                        certDao.addRelation(certId, optionalTag.get().getTagId());
                    }
                } else {
                    GiftCertificateTag tag = new GiftCertificateTag(tagName);
                    GiftCertificateTag newTagWithId = tagDao.add(tag);
                    certDao.addRelation(certId, newTagWithId.getTagId());
                }
            }
        }
    }
}
