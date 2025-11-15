package br.com.washington.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateRequest(

        String code,

        @NotBlank(message = "Description must not be blank")
        String description,

        @NotNull
        BigDecimal discountValue,

        @NotNull(message = "Expiration date must not be null")
        LocalDateTime expirationDate,

        Boolean published
) {
}
