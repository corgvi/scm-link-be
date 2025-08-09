package com.cvv.scm_link.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    Boolean isActive;
    LocalDate dob;
    Set<RoleResponse> roles;
}
