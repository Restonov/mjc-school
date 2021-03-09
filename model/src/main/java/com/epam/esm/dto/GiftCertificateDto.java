package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto {

    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @DecimalMin(value = "5.0")
    private double price;

    @Min(1)
    private int duration;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @NotEmpty(message = "Tag list cannot be empty")
    private Set<GiftCertificateTagDto> tags;

    public GiftCertificateDto(long id,
                              @NotBlank(message = "Name is mandatory") String name,
                              @NotBlank(message = "Description is mandatory") String description,
                              @DecimalMin(value = "5.0") double price,
                              @Min(1) int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }
}
