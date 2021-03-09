package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateTagDto {

    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    public GiftCertificateTagDto(@NotBlank(message = "Name is mandatory") String name) {
        this.name = name;
    }
}
