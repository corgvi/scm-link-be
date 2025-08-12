package com.cvv.scm_link.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_EXISTED(1001, HttpStatus.NOT_FOUND, "User already exists"),
    USER_NOT_FOUND(1002, HttpStatus.NOT_FOUND, "User not found"),
    UNCATEGORIZED(1003, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized"),
    UNAUTHORIZED(1004, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    UNAUTHENTICATED(1005, HttpStatus.FORBIDDEN, "Unauthenticated"),
    USERNAME_INVALID(1004, HttpStatus.BAD_REQUEST, "Username must be at least {min} and the most {max} character"),
    PASSWORD_INVALID(1005, HttpStatus.BAD_REQUEST, "Password must be at least {min} and the most {max} character"),
    EMAIL_INVALID(1006, HttpStatus.BAD_REQUEST, "Email is invalid"),
    PHONE_NUMBER_INVALID(1006, HttpStatus.BAD_REQUEST, "Phone number is invalid"),
    ROLE_IS_REQUIRED(1007, HttpStatus.BAD_REQUEST, "Role is required"),
    PERMISSION_IS_REQUIRED(1008, HttpStatus.BAD_REQUEST, "Permission is required"),
    DOB_INVALID(1009, HttpStatus.BAD_REQUEST, "Your age must be at least {min}"),
    PERMISSION_EXISTED(1010, HttpStatus.CONFLICT, "Permission already exists"),
    PERMISSION_NOT_EXISTED(1012, HttpStatus.NOT_FOUND, "Permission not found"),
    ROLE_EXISTED(1011, HttpStatus.CONFLICT, "Role already exists"),
    ROLE_NOT_EXISTED(1013, HttpStatus.NOT_FOUND, "Role not found"),
    GENERATE_TOKEN_FAILED(1014, HttpStatus.BAD_REQUEST, "Cannot generate token"),
    ENTITY_NOT_FOUND(1015, HttpStatus.NOT_FOUND, "Entity not found"),
    NAME_INVALID(1016, HttpStatus.BAD_REQUEST, "Name is invalid"),
    CODE_INVALID(1017, HttpStatus.BAD_REQUEST, "Code must be exactly {max} characters"),
    DTO_IS_NULL(1018, HttpStatus.BAD_REQUEST , "DTO must not be null" ),
    SUPPLIER_NOT_FOUND(1019, HttpStatus.NOT_FOUND , "Supplier not found" ),
    CATEGORY_NOT_FOUND(1020, HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_NOT_FOUND(1021, HttpStatus.NOT_FOUND, "Product not found"),
    SIZE_INVALID(1022, HttpStatus.BAD_REQUEST, "Size must be exactly {max} characters"),
    COLOR_INVALID(1023, HttpStatus.BAD_REQUEST, "Color is invalid"),
    CODE_EXISTED(1024, HttpStatus.BAD_REQUEST, "Code already existed");

    int code;
    HttpStatusCode httpStatusCode;
    String message;

    ErrorCode(int code, HttpStatusCode httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
