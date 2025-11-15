package br.com.washington.coupon.controller;

import br.com.washington.coupon.dto.request.CouponCreateRequest;
import br.com.washington.coupon.dto.response.CouponResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/coupon", "/api/v1/coupon"})
public class CouponController {

    @PostMapping
    public CouponResponse createCoupon(@RequestBody @Validated CouponCreateRequest request ) {
        return null;
    }
}
