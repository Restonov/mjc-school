package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserFormDto {

    @NotBlank(message = "Name is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
