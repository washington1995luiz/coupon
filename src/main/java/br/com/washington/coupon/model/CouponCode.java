package br.com.washington.coupon.model;

import br.com.washington.coupon.exception.CouponCodeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.ObjectUtils;

@Embeddable
public record CouponCode(@Column(name = "code", nullable = false, unique = true) String value) {
    public CouponCode {
        if (ObjectUtils.isEmpty(value)) {
            throw new CouponCodeException("Coupon code cannot be null or blank");
        }

        String cleaned = value.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (cleaned.length() != 6) {
            throw new CouponCodeException("Coupon code must contain exactly 6 alphanumeric characters");
        }

        value = cleaned;
    }
}
