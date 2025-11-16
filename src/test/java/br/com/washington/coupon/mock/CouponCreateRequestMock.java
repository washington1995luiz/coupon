package br.com.washington.coupon.mock;

import br.com.washington.coupon.dto.request.CouponCreateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponCreateRequestMock {

    public static CouponCreateRequest buildGoodValues() {
        return new CouponCreateRequest(
                "CODE10",
                "DESCRIPTION",
                new BigDecimal("10"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildGoodValuesCodeSpecialCharacters() {
        return new CouponCreateRequest(
                "COD&E*10",
                "DESCRIPTION",
                new BigDecimal("10"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildBadValuesCodeSpecialCharacters() {
        return new CouponCreateRequest(
                "COD&E*10T",
                "DESCRIPTION",
                new BigDecimal("10"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildBadValuesCodeSizeLessThanSix() {
        return new CouponCreateRequest(
                "CODE",
                "DESCRIPTION",
                new BigDecimal("10"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildBadValuesCodeSizeGreaterThanSix() {
        return new CouponCreateRequest(
                "CODE10000",
                "DESCRIPTION",
                new BigDecimal("10"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildBadValuesDiscountValue() {
        return new CouponCreateRequest(
                "CODE10",
                "DESCRIPTION",
                new BigDecimal("0.4"),
                LocalDateTime.parse("2025-12-01T10:00:00"),
                true);
    }

    public static CouponCreateRequest buildBadValuesExpirationDate() {
        return new CouponCreateRequest(
                "CODE10",
                "DESCRIPTION",
                new BigDecimal("0.5"),
                LocalDateTime.parse("2010-10-01T10:00:00"),
                true);
    }

}
