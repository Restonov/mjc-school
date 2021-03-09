package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Gift certificate resource
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificate")
@EqualsAndHashCode(exclude = {"tags", "orders"})
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private BigDecimal price;

    private int duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<GiftCertificateTag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "certificate")
    private Set<UserOrder> orders;

    public GiftCertificate(String name,
                           String description,
                           BigDecimal price,
                           int duration,
                           Set<GiftCertificateTag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * Pre persist audit
     */
    @PrePersist
    private void onPrePersist() {
        setCreateDate(LocalDateTime.now());
        setLastUpdateDate(LocalDateTime.now());
    }

    /**
     * Pre update audit
     */
    @PreUpdate
    private void onPreUpdate() {
        setLastUpdateDate(LocalDateTime.now());
    }
}
