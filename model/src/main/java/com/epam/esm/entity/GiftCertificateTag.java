package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Gift certificate Tag resource
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class GiftCertificateTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Tag name is mandatory")
    @Column(name = "name", unique = true, length = 20, nullable = false)
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> certificates;

    public GiftCertificateTag(String name) {
        this.name = name;
    }

    public GiftCertificateTag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificateTag tag = (GiftCertificateTag) o;

        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
