package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateTagService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * Gift certificate tag service implementation
 */
@Log4j2
@NoArgsConstructor
@Service("giftCertificateTagService")
public class GiftCertificateTagServiceImpl extends GiftCertificateTagService {

    /**
     * Instantiates a new tag service
     *
     * @param tagDao tag dao
     */
    public GiftCertificateTagServiceImpl(GiftCertificateTagDao tagDao) {
        super(tagDao);
    }

    @Transactional
    @Override
    public GiftCertificateTag create(GiftCertificateTag tag) {
        GiftCertificateTag createdTag;
        Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tag.getName());
        createdTag = optionalTag.orElseGet(() -> tagDao.add(tag));
        return createdTag;
    }

    @Override
    public List<GiftCertificateTag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public Optional<GiftCertificateTag> findById(long id) {
        Optional<GiftCertificateTag> optionalTag;
        try {
            optionalTag = tagDao.findById(id);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Requested resource not found (id = " + id + ")", e);
        }
        return optionalTag;
    }

    @Override
    public void delete(long id) {
            tagDao.delete(id);
    }
}
