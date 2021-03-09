package com.epam.esm.dao.mapper;

import com.epam.esm.dao.builder.GiftCertificateTagDaoBuilder;
import com.epam.esm.entity.GiftCertificateTag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map tag using tag builder
 */
public class GiftCertificateTagRowMapper implements RowMapper<GiftCertificateTag> {

    public GiftCertificateTag mapRow(@NonNull ResultSet set, int rowNum) throws SQLException {
        return GiftCertificateTagDaoBuilder.build(set);
    }
}
