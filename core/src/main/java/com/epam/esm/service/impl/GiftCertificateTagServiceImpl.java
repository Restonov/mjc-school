package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateTagService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Gift certificate tag service implementation
 */
@Log4j2
@Service("giftCertificateTagService")
@Transactional
public class GiftCertificateTagServiceImpl extends GiftCertificateTagService {

    /**
     * Constructor for test purposes
     *
     * @param tagDao  Tag dao
     * @param userDao User dao
     */
    public GiftCertificateTagServiceImpl(GiftCertificateTagDao tagDao, UserDao userDao) {
        this.tagDao = tagDao;
        this.userDao = userDao;
    }

    @Override
    public GiftCertificateTag create(GiftCertificateTag tag) {
        Optional<GiftCertificateTag> optionalTag = tagDao.findByName(tag.getName());
        return optionalTag.orElseGet(() -> tagDao.save(tag));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftCertificateTag> findAll(int currentPage, int pageSize) {
        Pageable pageAndResultPerPage = PageRequest.of(currentPage, pageSize);
        return tagDao.findAll(pageAndResultPerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateTag findById(long id) {
        GiftCertificateTag tag;
        Optional<GiftCertificateTag> optionalTag = tagDao.findById(id);
        if (optionalTag.isPresent()) {
            tag = optionalTag.get();
        } else {
            throw new ResourceNotFoundException("tag.not.found");
        }
        return tag;
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateTag findMostProfitableTag() {
        GiftCertificateTag tag;
        List<User> usersBySumOfOrders = userDao.findUsersBySumOfOrdersDesc();
        User user = usersBySumOfOrders.get(NumberUtils.INTEGER_ZERO);
        List<GiftCertificateTag> tags = new ArrayList<>();
        Set<UserOrder> orders = user.getOrders();
        for (UserOrder order : orders) {
            GiftCertificate certificate = order.getCertificate();
            Set<GiftCertificateTag> certTags = certificate.getTags();
            CollectionUtils.addAll(tags, certTags.iterator());
        }
        tag = tags.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(ResourceNotFoundException::new).getKey();
        return tag;
    }

    @Override
    public void delete(long id) {
        Optional<GiftCertificateTag> optionalTag = tagDao.findById(id);
        optionalTag.ifPresent(t -> tagDao.delete(t));
    }
}
