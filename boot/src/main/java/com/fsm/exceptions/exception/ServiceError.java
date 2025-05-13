package com.fsm.exceptions.exception;

public class ServiceError extends  RuntimeException {

    private final String message;
    private final int status;

    public ServiceError(String message, int status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public ServiceError(String message) {
        super(message);
        this.message = message;
        this.status = 400; // Default status code
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
