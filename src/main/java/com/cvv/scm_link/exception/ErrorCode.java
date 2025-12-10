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
    DTO_IS_NULL(1018, HttpStatus.BAD_REQUEST, "DTO must not be null"),
    SUPPLIER_NOT_FOUND(1019, HttpStatus.NOT_FOUND, "Supplier not found"),
    CATEGORY_NOT_FOUND(1020, HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_NOT_FOUND(1021, HttpStatus.NOT_FOUND, "Product not found"),
    SIZE_INVALID(1022, HttpStatus.BAD_REQUEST, "Size must be exactly {max} characters"),
    COLOR_INVALID(1023, HttpStatus.BAD_REQUEST, "Color is invalid"),
    CODE_EXISTED(1024, HttpStatus.BAD_REQUEST, "Code already existed"),
    FIELD_REQUIRED(1024, HttpStatus.BAD_REQUEST, "Field is required"),
    WAREHOUSE_NOT_FOUND(1025, HttpStatus.NOT_FOUND, "Warehouse not found"),
    WAREHOUSE_LOCATION_NOT_FOUND(1026, HttpStatus.NOT_FOUND, "Warehouse location not found"),
    INVENTORY_LEVEL_NOT_FOUND(1027, HttpStatus.NOT_FOUND, "Inventory level not found"),
    WAREHOUSE_LOCATION_NOT_IN_WAREHOUSE(1028, HttpStatus.NOT_FOUND, "Warehouse location not in Warehouse"),
    QUANTITY_IS_REQUIRED(1029, HttpStatus.BAD_REQUEST, "Quantity is required"),
    BATCH_NUMBER_IS_REQUIRED(1030, HttpStatus.BAD_REQUEST, "Batch number is required"),
    COST_PRICE_IS_REQUIRED(1031, HttpStatus.BAD_REQUEST, "Cost price is required"),
    INVENTORY_LEVEL_IS_REQUIRED(1032, HttpStatus.BAD_REQUEST, "Inventory level is required"),
    WAREHOUSE_IS_REQUIRED(1033, HttpStatus.BAD_REQUEST, "Warehouse is required"),
    PRODUCT_IS_REQUIRED(1034, HttpStatus.BAD_REQUEST, "Product is required"),
    TRANSACTION_TYPE_IS_REQUIRED(1035, HttpStatus.BAD_REQUEST, "Transaction type is required"),
    LOCATION_CODE_IS_REQUIRED(1036, HttpStatus.BAD_REQUEST, "Location is required"),
    LOCATION_TYPE_IS_REQUIRED(1037, HttpStatus.BAD_REQUEST, "Location type is required"),
    MAX_CAPACITY_IS_REQUIRED(1038, HttpStatus.BAD_REQUEST, "Max capacity is required"),
    IS_AVAILABLE_IS_REQUIRED(1039, HttpStatus.BAD_REQUEST, "Is available is required"),
    LATITUDE_INVALID(1040, HttpStatus.BAD_REQUEST, "Latitude is invalid"),
    LONGITUDE_INVALID(1041, HttpStatus.BAD_REQUEST, "Longitude is invalid"),
    RELATE_ID_IS_REQUIRED(1042, HttpStatus.BAD_REQUEST, "Relate id is required"),
    ADDRESS_IS_REQUIRED(1043, HttpStatus.BAD_REQUEST, "Address is required"),
    PRICE_INVALID(1044, HttpStatus.BAD_REQUEST, "Price is invalid"),
    ORDER_NOT_FOUND(1045, HttpStatus.NOT_FOUND, "Order not found"),
    PRODUCT_EXCEEDS_ALLOWABLE(1046, HttpStatus.BAD_REQUEST, "Product exceeds allowable quantity"),
    ROUTE_NOT_FOUND(1047, HttpStatus.BAD_REQUEST, "No route found from Mapbox API"),
    WEIGHT_INVALID(1048, HttpStatus.BAD_REQUEST, "Weight is invalid"),
    INVENTORY_LOCATION_DETAIL_NOT_FOUND(1049, HttpStatus.NOT_FOUND, "Inventory location detail not found"),
    ADDRESS_NOT_FOUND(1050, HttpStatus.NOT_FOUND, "Address not found"),
    ORDER_COMPLETED(1051, HttpStatus.BAD_REQUEST, "Order completed"),
    ORDER_CANCELLED(1052, HttpStatus.BAD_REQUEST, "Order cancelled"),
    PAYMENT_STATUS_INVALID(1053, HttpStatus.BAD_REQUEST, "Payment status is invalid"),
    LICENSE_PLATE_NOT_FOUND(1054, HttpStatus.NOT_FOUND, "License plate not found"),
    DELIVERY_ALREADY_FINALIZED(1055, HttpStatus.BAD_REQUEST, "Delivery already finalized"),
    DELIVERY_NOT_FOUND(1056, HttpStatus.NOT_FOUND, "Delivery not found"),
    CANNOT_UPDATE_ORDERS_WHILE_DELIVERING(1057, HttpStatus.BAD_REQUEST, "Cannot update orders while delivering"),
    ROUTE_CALCULATION_FAILED(1058, HttpStatus.BAD_REQUEST, "Route calculation failed"),
    INVALID_REQUEST(1059, HttpStatus.BAD_REQUEST, "DeliveryOrders list cannot be null or empty"),
    DUPLICATE_ORDER_IN_DELIVERY(1060, HttpStatus.BAD_REQUEST, "Duplicate order"),
    INVALID_DELIVERY_STATUS(1061, HttpStatus.BAD_REQUEST, "Invalid delivery status");

    int code;
    HttpStatusCode httpStatusCode;
    String message;

    ErrorCode(int code, HttpStatusCode httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
