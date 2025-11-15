package br.com.washington.coupon.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateRequest(
   @Max(value = 6, message = "Code must have at most 6 characters") String code,
   @NotBlank(message = "Description must not be blank") String description,
   @NotNull @DecimalMin(value = "0.5", message = "Discount value must be greater than or equal to 0.5")
   BigDecimal discountValue,
   @NotNull(message = "Expiration date must not be null")
   LocalDateTime expirationDate,
   Boolean publish
) {
    public CouponCreateRequest {
        if (publish == null) {
            publish = false;
        }
    }
}
