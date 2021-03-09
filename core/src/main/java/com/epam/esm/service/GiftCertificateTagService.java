package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificateTag;

public abstract class GiftCertificateTagService extends AbstractService<GiftCertificateTag> {

    /**
     * First of all find the Most profitable User
     * then find the most frequently Tag in User's orders
     *
     * @return Most profitable Tag
     */
    public abstract GiftCertificateTag findMostProfitableTag();

}
