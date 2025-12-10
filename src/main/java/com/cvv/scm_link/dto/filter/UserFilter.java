package com.cvv.scm_link.dto.filter;

import java.util.Set;

import com.cvv.scm_link.dto.BaseFilter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilter extends BaseFilter {
    String username;
    String email;
    String fullName;
    String phoneNumber;
    String city;
    Boolean isActive;
    Set<String> roles;
}
