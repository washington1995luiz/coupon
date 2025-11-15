package br.com.washington.coupon.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CouponIdNotFoundException extends ResponseStatusException {
    public CouponIdNotFoundException(String message) {
        super(NOT_FOUND, message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
