package com.example.onlinebanksystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "FIN cannot be blank")
    @Size(min = 7, max = 7, message = "FIN must be 7 characters long.")
    @Pattern(regexp = "^[0-9A-Z]{7}$", message = "FIN must consist of 7 uppercase letters or digits.")
    private String fin;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}