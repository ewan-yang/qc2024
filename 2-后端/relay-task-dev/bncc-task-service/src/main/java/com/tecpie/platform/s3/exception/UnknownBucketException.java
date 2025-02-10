package com.tecpie.platform.s3.exception;

public class UnknownBucketException extends BaseException {

    private String code;
    private String msg;

    public UnknownBucketException() {
    }

    public UnknownBucketException(String msg) {
        super(msg);
    }
}
