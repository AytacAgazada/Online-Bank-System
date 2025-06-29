package com.example.onlinebanksystem.model.dto;

import com.example.onlinebanksystem.validation.OneOfFieldsNotBlank;
import com.example.onlinebanksystem.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@PasswordMatches
@OneOfFieldsNotBlank(fieldNames = {"email","phone"}, message = "At least one of the fields must be filled in.")
@Data
public class SignupRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "FIN cannot be blank")
    @Size(min = 7, max = 7, message = "FIN must be 7 characters long.")
    @Pattern(regexp = "^[0-9A-Z]{7}$", message = "FIN must consist of 7 uppercase letters or digits.")
    private String fin;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\+994\\d{9}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character (!@#$%^&*()_+) and be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank")
    private String confirmPassword;

}
