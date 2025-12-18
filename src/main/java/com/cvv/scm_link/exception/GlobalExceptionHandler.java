package com.cvv.scm_link.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cvv.scm_link.dto.response.APIResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleRuntimeException(Exception e) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        log.error(ErrorCode.UNCATEGORIZED.getMessage(), e);
        return ResponseEntity.internalServerError().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        log.error(errorCode.getMessage(), e);
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        log.error(errorCode.getMessage(), e);
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolations =
                    e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolations.getConstraintDescriptor().getAttributes();
            log.info("ConstraintViolations {}", constraintViolations);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttributes(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    private String mapAttributes(String message, Map<String, Object> attributes) {
        if (Objects.isNull(attributes)) {
            return message;
        }
        String finalMessage = message;
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));

        if (minValue != null) {
            finalMessage = finalMessage.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        }

        if (maxValue != null) {
            finalMessage = finalMessage.replace("{" + MAX_ATTRIBUTE + "}", maxValue);
        }
        return finalMessage;
    }

    @ExceptionHandler(value = TransactionSystemException.class)
    ResponseEntity<APIResponse> handleTransactionSystemException(TransactionSystemException e) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        Map<String, Object> attributes = null;

        // TransactionSystemException thường bọc ConstraintViolationException bên trong
        Throwable cause = e.getRootCause();

        if (cause instanceof ConstraintViolationException constraintViolationException) {
            // Lấy lỗi đầu tiên
            var violation = constraintViolationException.getConstraintViolations().stream()
                    .findFirst()
                    .orElse(null);

            if (violation != null) {
                // Lấy message template (chính là cái enum key như "EXPIRY_DATE_INVALID")
                String enumKey = violation.getMessageTemplate();
                try {
                    errorCode = ErrorCode.valueOf(enumKey);
                    // Lấy các attribute của annotation (ví dụ: giá trị min, max trong validate)
                    attributes = violation.getConstraintDescriptor().getAttributes();
                } catch (IllegalArgumentException ex) {
                    log.warn("Enum key {} not found in ErrorCode", enumKey);
                }
            }
        }

        log.error("Transaction System Error: ", e);

        APIResponse apiResponse = APIResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes)
                        ? mapAttributes(errorCode.getMessage(), attributes)
                        : errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }
}
