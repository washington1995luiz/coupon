package br.com.washington.coupon.model;

import br.com.washington.coupon.exception.DiscountValueException;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private CouponCode code;

    @Column(name = "description", nullable = false)
    private String description;

    @DecimalMin(value = "0.5", message = "Discount value must be greater than or equal to 0.5")
    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "redeemed", nullable = false)
    private boolean redeemed;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    private static final BigDecimal MIN_DISCOUNT = new BigDecimal("0.5");


    public static Coupon create(String code, String description, BigDecimal discountValue, LocalDateTime expirationDate, Boolean published) {
        if (discountValue.compareTo(MIN_DISCOUNT) < 0) {
            throw new DiscountValueException("Discount value must be greater than or equal to " + MIN_DISCOUNT);
        }

        var now = LocalDateTime.now();

        Coupon coupon = new Coupon();
        coupon.code = new CouponCode(code);
        coupon.description = description;
        coupon.discountValue = discountValue;
        coupon.expirationDate = expirationDate;
        coupon.status = Status.ACTIVE;
        coupon.published = published != null && published;
        coupon.redeemed = false;
        coupon.createdAt = now;
        coupon.updatedAt = now;
        return coupon;
    }

    public void softDelete() {
        var now = LocalDateTime.now();
        this.status = Status.DELETED;
        this.updatedAt = now;
        this.deletedAt = now;
    }

}
