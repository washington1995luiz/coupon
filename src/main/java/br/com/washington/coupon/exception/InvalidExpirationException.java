package br.com.washington.coupon.exception;

public class InvalidExpirationException extends RuntimeException {
    public InvalidExpirationException(String msg) {
        super(msg);
    }
}
