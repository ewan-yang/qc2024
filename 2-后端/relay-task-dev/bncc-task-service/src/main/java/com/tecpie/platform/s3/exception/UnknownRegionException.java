package com.tecpie.platform.s3.exception;

public class UnknownRegionException extends BaseException {

    private String code;
    private String msg;

    public UnknownRegionException() {
    }

    public UnknownRegionException(String msg) {
        super(msg);
    }
}
