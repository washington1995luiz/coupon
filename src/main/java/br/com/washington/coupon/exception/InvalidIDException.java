package br.com.washington.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidIDException extends ResponseStatusException {
    public InvalidIDException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
