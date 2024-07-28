package org.yascode.spring_webflux_reactive.exception;

public class MultipleResourcesFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MultipleResourcesFoundException(String msg) {
        super(msg);
    }
}
