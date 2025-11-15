package br.com.washington.coupon.event.dto;

import java.util.UUID;

public record CouponCreatedEvent(UUID id, String code) {
}
