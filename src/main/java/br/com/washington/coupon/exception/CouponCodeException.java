package br.com.washington.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CouponCodeException extends ResponseStatusException {
    public CouponCodeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
