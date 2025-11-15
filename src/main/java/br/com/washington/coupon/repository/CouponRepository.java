package br.com.washington.coupon.repository;

import br.com.washington.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    boolean existsByCode_value(String value);

    Optional<Coupon> findByIdAndDeletedAtIsNull(UUID id);
}
