package com.cvv.scm_link.dto.request;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import com.cvv.scm_link.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Email(message = "EMAIL_INVALID")
    String email;

    String fullName;

    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    String address;
    Boolean isActive;

    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;

    Set<String> roles;
}
