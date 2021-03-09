package com.epam.esm.dao.impl;

import com.epam.esm.dao.builder.GiftCertificateDaoBuilder;
import com.epam.esm.dao.mapper.GiftCertificateRowMapper;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.RelationMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Gift certificate dao implementation
 */
@Repository
public class GiftCertificateDaoImpl extends GiftCertificateDao {
    private static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String INSERT_NEW_CERTIFICATE ="INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_ALL_CERTIFICATES = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";
    private static final String SELECT_CERTIFICATE_BY_TAG_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate AS gc INNER JOIN gift_certificate_tag AS gct ON gc.id = gct.gift_certificate_id WHERE gct.tag_id = ?";
    private static final String SELECT_BY_KEYWORD = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE name ILIKE ? OR description ILIKE ?";
    public static final String INSERT_NEW_RELATION = "INSERT INTO gift_certificate_tag VALUES (?, ?)";
    public static final String SELECT_RELATION = "SELECT gift_certificate_id, tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    private static final String UPDATE_CERTIFICATE = "UPDATE gift_certificate SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private final JdbcOperations jdbcOperations;

    /**
     * Instantiates a new Tag dao
     *
     * @param jdbcOperations jdbc template interface
     */
    @Autowired
    public GiftCertificateDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_NEW_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setDouble(3, certificate.getPrice().doubleValue());
            ps.setInt(4, certificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(certificate.getCreateDate()));
            ps.setTimestamp(6, Timestamp.valueOf(certificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);
        certificate.setCertificateId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return certificate;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcOperations.query(SELECT_ALL_CERTIFICATES, new GiftCertificateRowMapper());
    }

    @Override
    public Optional<GiftCertificate> findById(long id){
        return Optional.ofNullable(jdbcOperations.queryForObject(
                SELECT_CERTIFICATE_BY_ID, new GiftCertificateRowMapper(), id));
    }

    @Override
    public List<GiftCertificate> findByTagId(long tagId) {
        return jdbcOperations.query(SELECT_CERTIFICATE_BY_TAG_ID, new Object[]{tagId},
                (rs, rowNum) -> GiftCertificateDaoBuilder.buildFromSet(rs));
    }

    @Override
    public List<GiftCertificate> findByKeyword(String keyWord){
        return jdbcOperations.query(SELECT_BY_KEYWORD, new Object[]{keyWord, keyWord},
                (rs, rowNum) -> GiftCertificateDaoBuilder.buildFromSet(rs));
    }

    @Override
    public void update(GiftCertificate certificate){
        jdbcOperations.update(UPDATE_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice().doubleValue(),
                certificate.getDuration(),
                Timestamp.valueOf(certificate.getLastUpdateDate()),
                certificate.getCertificateId()
        );
    }

    @Override
    public void addRelation(long certificateId, long tagId){
        jdbcOperations.update( INSERT_NEW_RELATION, certificateId, tagId );
    }

    @Override
    public boolean findRelation(long certificateId, long tagId){
        boolean relationExists = false;
        try {
            Map<Long, Long> relation = jdbcOperations.queryForObject(
                    SELECT_RELATION, new RelationMapper(), certificateId, tagId);
            if (relation != null && !relation.isEmpty()) {
                relationExists = true;
            }
        } catch (DataAccessException ignored) {}
        return relationExists;
    }

    @Override
    public void delete(long id){
        jdbcOperations.update( DELETE_CERTIFICATE, id );
    }
}
