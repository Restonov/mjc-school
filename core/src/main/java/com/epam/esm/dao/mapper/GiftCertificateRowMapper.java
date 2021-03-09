package com.epam.esm.dao.mapper;

import com.epam.esm.dao.builder.GiftCertificateDaoBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Map certificate using certificate builder
 */
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    public GiftCertificate mapRow(@NonNull ResultSet set, int rowNum) throws SQLException {
        return GiftCertificateDaoBuilder.buildFromSet(set);
    }
}
