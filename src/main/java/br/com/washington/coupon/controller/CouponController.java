package br.com.washington.coupon.controller;

import br.com.washington.coupon.dto.request.CouponCreateRequest;
import br.com.washington.coupon.dto.response.CouponResponse;
import br.com.washington.coupon.exception.DiscountValueException;
import br.com.washington.coupon.exception.InvalidIDException;
import br.com.washington.coupon.model.Coupon;
import br.com.washington.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CouponResponse createCoupon(@RequestBody @Valid CouponCreateRequest request) {
        if (request.discountValue().compareTo(new BigDecimal("0.5")) < 0) {
            throw new DiscountValueException("Discount value must be greater than or equal to 0.5");
        }
        Coupon coupon = couponService.create(request);
        return CouponResponse.fromModel(coupon);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CouponResponse getCouponById(@PathVariable String id) {
        Coupon coupon = couponService.findById(parseUUID(id));
        return CouponResponse.fromModel(coupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCouponById(@PathVariable String id) {
        couponService.delete(parseUUID(id));
    }

    private UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidIDException("Invalid ID format");
        }
    }
}
