package br.com.washington.coupon.exception;

public class InvalidIDException extends RuntimeException {
    public InvalidIDException(String msg) {
        super(msg);
    }
}
