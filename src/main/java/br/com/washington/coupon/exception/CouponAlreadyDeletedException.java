package br.com.washington.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CouponAlreadyDeletedException extends ResponseStatusException {
    public CouponAlreadyDeletedException(String text) {
        super(HttpStatus.CONFLICT, text);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
