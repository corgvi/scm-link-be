package com.cvv.scm_link.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "NAME_INVALID")
    String customerName;

    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String customerPhone;

    @Email(message = "EMAIL_INVALID")
    String customerEmail;

    @NotBlank(message = "ADDRESS_IS_REQUIRED")
    String shippingAddress;

    String note;
    List<OrderItemsRequest> items;
}
