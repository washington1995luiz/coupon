package br.com.washington.coupon.service;

import br.com.washington.coupon.dto.request.CouponCreateRequest;
import br.com.washington.coupon.model.Coupon;

import java.util.UUID;

public interface CouponService {
    Coupon create(CouponCreateRequest request);
    Coupon findById(UUID id);
    void delete(UUID id);
}