package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificateTag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Gift certificate tag service
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class GiftCertificateTagService extends AbstractService<GiftCertificateTag> {

    @Autowired protected GiftCertificateTagDao tagDao;
}
