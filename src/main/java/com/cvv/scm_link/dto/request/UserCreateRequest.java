package com.cvv.scm_link.dto.request;

import java.time.LocalDate;

import com.cvv.scm_link.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {

    @Size(min = 3, max = 100, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    String password;

    @Email(message = "EMAIL_INVALID")
    String email;

    String fullName;

    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String phoneNumber;
    String address;

    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
}
