package com.central.common.exception;

public class ServiceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6610083281801529147L;

    public ServiceNotFoundException(String message) {
        super(message);
    }
}
