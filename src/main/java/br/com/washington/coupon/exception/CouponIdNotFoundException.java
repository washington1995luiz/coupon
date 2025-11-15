package br.com.washington.coupon.exception;

public class CouponIdNotFoundException extends RuntimeException {
    public CouponIdNotFoundException(String msg) {
        super(msg);
    }

}
