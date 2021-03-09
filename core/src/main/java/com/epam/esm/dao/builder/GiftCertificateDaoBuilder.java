package com.epam.esm.dao.builder;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.GiftCertificate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Gift certificate dao builder
 */
public class GiftCertificateDaoBuilder {

    private GiftCertificateDaoBuilder() {
    }

    /**
     * Build GiftCertificate from ResultSet
     *
     * @param set ResultSet
     * @return GiftCertificate
     * @throws SQLException sql exception
     */
    public static GiftCertificate buildFromSet(ResultSet set) throws SQLException {
        long id = set.getLong(ParameterName.ID);
        String name = set.getString(ParameterName.NAME);
        String description = set.getString(ParameterName.DESCRIPTION);
        BigDecimal price = BigDecimal.valueOf(set.getDouble(ParameterName.PRICE));
        int duration = set.getInt(ParameterName.DURATION);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setCertificateId(id);
        certificate.setName(name);
        certificate.setDescription(description);
        certificate.setPrice(price);
        certificate.setDuration(duration);
        certificate.setCreateDate(set.getTimestamp(ParameterName.CREATE_DATE).toLocalDateTime());
        certificate.setLastUpdateDate(set.getTimestamp(ParameterName.LAST_UPDATE_DATE).toLocalDateTime());
        return certificate;
    }
}
