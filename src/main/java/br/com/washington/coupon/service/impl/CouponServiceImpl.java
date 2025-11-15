package br.com.washington.coupon.service.impl;

import br.com.washington.coupon.dto.request.CouponCreateRequest;
import br.com.washington.coupon.event.dto.CouponAlreadyDeletedEvent;
import br.com.washington.coupon.event.dto.CouponAlreadyExistsEvent;
import br.com.washington.coupon.event.dto.CouponCreatedEvent;
import br.com.washington.coupon.event.dto.CouponDeletedEvent;
import br.com.washington.coupon.exception.CouponAlreadyDeletedException;
import br.com.washington.coupon.exception.CouponAlreadyExistsException;
import br.com.washington.coupon.exception.CouponIdNotFoundException;
import br.com.washington.coupon.model.Coupon;
import br.com.washington.coupon.model.Status;
import br.com.washington.coupon.repository.CouponRepository;
import br.com.washington.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Coupon create(CouponCreateRequest request) {
        verifyIfCouponAlreadyExists(request);
        Coupon coupon = Coupon.create(request.code(), request.description(), request.discountValue(), request.expirationDate(), request.published());
        couponRepository.save(coupon);
        eventPublisher.publishEvent(new CouponCreatedEvent(coupon.getId(), coupon.getCode().value()));
        return coupon;
    }

    @Override
    public Coupon findById(UUID id) {
        return couponRepository.findById(id).orElseThrow(() -> new CouponIdNotFoundException("Coupon not found for this ID"));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Coupon coupon = findById(id);
        verifyIfCouponIsAlreadyDeleted(coupon);

        coupon.softDelete();
        couponRepository.save(coupon);

        eventPublisher.publishEvent(new CouponDeletedEvent(coupon.getId(), coupon.getCode().value()));
    }

    private void verifyIfCouponIsAlreadyDeleted(Coupon coupon) {
        if(coupon.getStatus() == Status.DELETED) {
            eventPublisher.publishEvent(new CouponAlreadyDeletedEvent(coupon.getId(), coupon.getCode().value()));
            throw new CouponAlreadyDeletedException("Coupon is already deleted");
        }
    }

    private boolean couponExists(String code) {
        return couponRepository.existsByCode_value(code);
    }

    private void verifyIfCouponAlreadyExists(CouponCreateRequest request) {
        if (couponExists(request.code())) {
            eventPublisher.publishEvent(new CouponAlreadyExistsEvent(request.code()));
            throw new CouponAlreadyExistsException("Coupon code already exists");
        }
    }
}
