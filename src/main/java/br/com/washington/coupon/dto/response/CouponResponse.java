package br.com.washington.coupon.dto.response;

import br.com.washington.coupon.model.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    LocalDateTime expirationDate,
    Status status,
    Boolean published,
    Boolean redeemed
) {
}