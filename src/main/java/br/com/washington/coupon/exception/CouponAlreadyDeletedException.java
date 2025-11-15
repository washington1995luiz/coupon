package br.com.washington.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException(String text) {
        super(text);
    }

}
