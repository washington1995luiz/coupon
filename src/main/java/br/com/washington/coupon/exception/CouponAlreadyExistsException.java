package br.com.washington.coupon.exception;

public class CouponAlreadyExistsException extends RuntimeException {
    public CouponAlreadyExistsException(String msg) {
        super(msg);
    }

}
