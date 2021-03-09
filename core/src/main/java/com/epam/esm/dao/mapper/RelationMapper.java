package com.epam.esm.dao.mapper;

import com.epam.esm.entity.ParameterName;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Map cert-tag relation
 */
public class RelationMapper implements RowMapper<Map<Long, Long>> {

    public Map<Long, Long> mapRow(@NonNull ResultSet set, int rowNum) throws SQLException {
        Map<Long, Long> relation = new HashMap<>();
        relation.put(set.getLong(ParameterName.GIFT_CERTIFICATE_ID), set.getLong(ParameterName.TAG_ID));
        return relation;
    }
}
