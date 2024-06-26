package com.qly.mallchat.common.common.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode ,String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
