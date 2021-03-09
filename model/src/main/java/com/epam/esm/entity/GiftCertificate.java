package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Gift certificate resource
 */
@Data
@EqualsAndHashCode(exclude = {"tags", "orders"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificate")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull
    @DecimalMin(value = "5.0")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal price;

    @Min(1)
    @NotNull
    private int duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotEmpty(message = "Tag list cannot be empty")
    @JoinTable(
            name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<GiftCertificateTag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "certificate")
    private Set<UserOrder> orders;

    public GiftCertificate(@NotBlank(message = "Name is mandatory") String name,
                           @NotBlank(message = "Description is mandatory") String description,
                           @NotNull @DecimalMin(value = "5.0") BigDecimal price,
                           @NotNull @Min(1) int duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public GiftCertificate(@NotBlank(message = "Name is mandatory") String name,
                           @NotBlank(message = "Description is mandatory") String description,
                           @NotNull @DecimalMin(value = "5.0") BigDecimal price,
                           @NotNull @Min(1) int duration,
                           LocalDateTime createDate,
                           LocalDateTime lastUpdateDate,
                           @NotEmpty(message = "Tag list cannot be empty") Set<GiftCertificateTag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
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
