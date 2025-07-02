package com.app.exception;

public class ErrorException {
    private String message;
    private int code;
    public ErrorException(){}
    public ErrorException(String message, int code) {
        this.message = message;
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public int getCode() {
        return code;
    }
}
