package com.cvv.scm_link.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends BaseDTO {
    String username;
    String email;
    String fullName;
    String phoneNumber;
    String imageUrl;
    String address;
    Boolean isActive;
    LocalDate dob;
    Set<RoleResponse> roles;
}
