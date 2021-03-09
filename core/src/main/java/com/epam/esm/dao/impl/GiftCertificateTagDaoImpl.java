package com.epam.esm.dao.impl;

import com.epam.esm.dao.mapper.GiftCertificateTagRowMapper;
import com.epam.esm.dao.builder.GiftCertificateTagDaoBuilder;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.dao.GiftCertificateTagDao;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Gift certificate tag dao implementation
 */
@Log4j2
@Repository
public class GiftCertificateTagDaoImpl extends GiftCertificateTagDao {
    private static final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    public static final String INSERT_NEW_TAG = "INSERT INTO tag (name) VALUES (?) RETURNING id";
    public static final String SELECT_ALL_TAGS = "SELECT id, name FROM tag";
    public static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = ?";
    public static final String SELECT_TAG_BY_TAG_NAME = "SELECT id, name FROM tag WHERE name = ?";
    public static final String SELECT_TAG_BY_CERTIFICATE_ID = "SELECT tag.id, tag.name FROM tag INNER JOIN gift_certificate_tag gct ON tag.id = gct.tag_id WHERE gct.gift_certificate_id = ?";
    private final JdbcOperations jdbcOperations;

    /**
     * Instantiates a new Gift certificate tag dao
     *
     * @param jdbcOperations jdbc template interface
     */
    @Autowired
    public GiftCertificateTagDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public GiftCertificateTag add(GiftCertificateTag tag){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_NEW_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        tag.setTagId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public List<GiftCertificateTag> findAll(){
        return jdbcOperations.query(SELECT_ALL_TAGS, new GiftCertificateTagRowMapper());
    }

    @Override
    public Optional<GiftCertificateTag> findById(long id) {
        return Optional.ofNullable(jdbcOperations.queryForObject(SELECT_TAG_BY_ID, new Object[]{id},
                (rs, rowNum) -> GiftCertificateTagDaoBuilder.build(rs)));
    }

    @Override
    public Optional<GiftCertificateTag> findByName(String tagName){
        Optional<GiftCertificateTag> optionalTag;
        try {
            optionalTag = Optional.ofNullable(
                    jdbcOperations.queryForObject(SELECT_TAG_BY_TAG_NAME, new Object[]{tagName},
                            (rs, rowNum) -> GiftCertificateTagDaoBuilder.build(rs)));
        } catch (DataAccessException e) {
            optionalTag = Optional.empty();
        }
        return optionalTag;
    }

    @Override
    public List<GiftCertificateTag> findByCertificateId(long certificateId){
        return jdbcOperations.query(SELECT_TAG_BY_CERTIFICATE_ID, new Object[]{certificateId},
                (rs, rowNum) -> GiftCertificateTagDaoBuilder.build(rs));
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update( DELETE_TAG, id );
    }
}
