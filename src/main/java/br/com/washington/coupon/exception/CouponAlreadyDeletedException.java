package br.com.washington.coupon.exception;

public class CouponAlreadyDeletedException extends RuntimeException{
    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
