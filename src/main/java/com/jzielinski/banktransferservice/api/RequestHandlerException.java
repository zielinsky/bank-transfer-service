package com.jzielinski.banktransferservice.api;

public class RequestHandlerException extends RuntimeException {
    private final int responseStatusCode;

    public RequestHandlerException(int responseStatusCode, String message, Throwable cause) {
        super(message, cause);
        this.responseStatusCode = responseStatusCode;
    }

    public RequestHandlerException(int responseStatusCode, String message) {
        super(message);
        this.responseStatusCode = responseStatusCode;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }
}
