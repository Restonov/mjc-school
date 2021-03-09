package com.epam.esm.dao.builder;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificateTag;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Gift certificate tag dao builder.
 */
public class GiftCertificateTagDaoBuilder {

    private GiftCertificateTagDaoBuilder() {
    }

    /**
     * Build gift certificate tag from ResultSet
     *
     * @param set ResultSet
     * @return gift certificate tag
     * @throws SQLException sql exception
     */
    public static GiftCertificateTag build(ResultSet set) throws SQLException {
        GiftCertificateTag tag = new GiftCertificateTag();
        tag.setTagId(set.getLong(ParameterName.ID));
        tag.setName(set.getString(ParameterName.NAME));
        return tag;
    }
}
