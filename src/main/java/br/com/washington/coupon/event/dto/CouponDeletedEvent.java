package br.com.washington.coupon.event.dto;

import java.util.UUID;

public record CouponDeletedEvent(UUID id, String code) {
}
