package com.cvv.scm_link.exception;

import lombok.Getter;
import lombok.Setter;

public class AppException extends RuntimeException {

    @Getter
    @Setter
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
