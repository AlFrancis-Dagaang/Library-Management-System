package com.app.util;

public class Response<T> {
    public int status;
    public String message;
    public Object data;

    public Response() {}
    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
