package com.cvv.scm_link.dto;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseFilter {
    String keyword;
    String sort = "createdAt,desc";
    Integer page = 1;
    Integer size = 10;
    LocalDateTime fromDate;
    LocalDateTime toDate;
}
