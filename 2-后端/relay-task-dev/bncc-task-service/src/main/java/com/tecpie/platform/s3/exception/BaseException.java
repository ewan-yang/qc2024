package com.tecpie.platform.s3.exception;

public class BaseException extends RuntimeException {

    private String code;
    private String msg;

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
