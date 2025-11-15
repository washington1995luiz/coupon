package br.com.washington.coupon.event.dto;

import java.util.UUID;

public record CouponAlreadyDeletedEvent(UUID id, String code) {
}
