package br.com.washington.coupon.exception;

public class ExpirationDateException extends RuntimeException {
    public ExpirationDateException(String msg) {
        super(msg);
    }
}
