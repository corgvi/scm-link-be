package com.cvv.scm_link.dto.request;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String email;
    String fullName;
    String phoneNumber;
    String address;
    LocalDate dob;
    Set<String> roles;
}
