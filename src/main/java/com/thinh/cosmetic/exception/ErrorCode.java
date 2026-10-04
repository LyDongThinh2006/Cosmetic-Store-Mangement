package com.thinh.cosmetic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed for request payload"),
    EMPTY_CART(HttpStatus.BAD_REQUEST, "Cart is empty"),
    PAYMENT_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "Payment method is not supported"),
    DELIVERY_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "Delivery method is not supported"),
    CONFIRM_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Confirm password does not match"),

    // 401 & 403
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "User is unauthenticated"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is denied"),

    // 404 Not Found
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource not found"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    SKU_NOT_FOUND(HttpStatus.NOT_FOUND, "Product SKU not found"),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "Store not found"),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer not found"),
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "Employee not found"),
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "Brand not found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),

    // 409 Conflict
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Email already exists"),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "Phone number already exists"),
    DUPLICATE_SKU_CODE(HttpStatus.CONFLICT, "SKU code already exists"),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "Insufficient stock for requested item"),
    NO_STORE_CAN_FULFILL(HttpStatus.CONFLICT, "No store can fulfill this order"),
    SKU_UNAVAILABLE(HttpStatus.CONFLICT, "SKU is currently unavailable"),
    PRICE_CHANGED(HttpStatus.CONFLICT, "Product price has changed"),
    INVALID_STATUS_TRANSITION(HttpStatus.CONFLICT, "Invalid status transition"),
    ORDER_NOT_CANCELLABLE(HttpStatus.CONFLICT, "Order cannot be cancelled in its current state"),
    DATA_CONFLICT(HttpStatus.CONFLICT, "Data conflict occurred"),

    // 500 Internal Server Error
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }
}
