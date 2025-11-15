package br.com.washington.coupon.model;

import br.com.washington.coupon.exception.CouponAlreadyDeletedException;
import jakarta.persistence.*;
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

    @Embedded @Column(name = "code", nullable = false, unique = true)
    private CouponCode code;

    @Column(name = "description", nullable = false)
    private String description;

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

    @Column(updatable = false, nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(updatable = false, nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;


    public Coupon create(String code, String description, BigDecimal discountValue, LocalDateTime expirationDate, Boolean published) {
        var now = LocalDateTime.now();
        this.code = new CouponCode(code);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = Status.ACTIVE;
        this.published = published != null && published;
        this.redeemed = false;
        this.createdAt = now;
        this.updatedAt = now;
        return this;
    }

    public void softDelete(){
        if (this.status == Status.DELETED) {
            throw new CouponAlreadyDeletedException("Coupon is already deleted");
        }
        var now = LocalDateTime.now();
        this.status = Status.DELETED;
        this.updatedAt = now;
        this.deletedAt = now;
    }

}
