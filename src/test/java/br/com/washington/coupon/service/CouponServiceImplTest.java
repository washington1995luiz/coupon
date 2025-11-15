package br.com.washington.coupon.service;

import br.com.washington.coupon.event.dto.CouponAlreadyExistsEvent;
import br.com.washington.coupon.event.dto.CouponCreatedEvent;
import br.com.washington.coupon.event.dto.CouponDeletedEvent;
import br.com.washington.coupon.exception.CouponAlreadyExistsException;
import br.com.washington.coupon.exception.CouponIdNotFoundException;
import br.com.washington.coupon.mock.CouponCreateRequestMock;
import br.com.washington.coupon.model.Coupon;
import br.com.washington.coupon.model.Status;
import br.com.washington.coupon.repository.CouponRepository;
import br.com.washington.coupon.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CouponServiceImpl couponService;

    @Captor
    private ArgumentCaptor<Coupon> couponCaptor;

    @Test
    public void shouldCreateCouponSuccessfully() {
        var request = CouponCreateRequestMock.buildGoodValues();

        when(couponRepository.existsByCode_value(request.code())).thenReturn(false);

        var saved = Coupon.create(request.code(), request.description(), request.discountValue(),
                request.expirationDate(), request.published());

        when(couponRepository.save(any(Coupon.class))).thenReturn(saved);

        var result = couponService.create(request);

        assertNotNull(result);
        assertEquals(request.code(), result.getCode().value());
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.discountValue(), result.getDiscountValue());
        assertEquals(request.expirationDate(), result.getExpirationDate());
        assertEquals(request.published(), result.isPublished());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertFalse(result.isRedeemed());


        verify(eventPublisher).publishEvent(any(CouponCreatedEvent.class));
        verify(couponRepository).save(couponCaptor.capture());

        var captured = couponCaptor.getValue();
        assertNotNull(captured);
        assertEquals(request.code(), captured.getCode().value());
    }

    @Test
    public void shouldThrowException_whenCouponAlreadyExists() {
        var request = CouponCreateRequestMock.buildGoodValues();
        when(couponRepository.existsByCode_value(request.code())).thenReturn(true);

        assertThrows(CouponAlreadyExistsException.class, () -> couponService.create(request));
        verify(couponRepository).existsByCode_value(request.code());
        verify(couponRepository, never()).save(any(Coupon.class));
        verify(eventPublisher).publishEvent(any(CouponAlreadyExistsEvent.class));
    }

    @Test
    public void shouldReturnCoupon_whenCouponExists() {
        var id = UUID.randomUUID();
        var request = CouponCreateRequestMock.buildGoodValues();
        var coupon = Coupon.create(request.code(), request.description(), request.discountValue(),
                request.expirationDate(), request.published());

        when(couponRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(coupon));
        var result = couponService.findById(id);

        assertNotNull(result);
        assertEquals(request.code(), result.getCode().value());
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.discountValue(), result.getDiscountValue());
        assertEquals(request.expirationDate(), result.getExpirationDate());
        assertEquals(request.published(), result.isPublished());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertFalse(result.isRedeemed());
    }

    @Test
    public void shouldThrowException_whenCouponDoesNotExist() {
        var id = UUID.randomUUID();
        when(couponRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());
        assertThrows(CouponIdNotFoundException.class, () -> couponService.findById(id));
    }

    @Test
    public void shouldDeleteCouponSuccessfully() {
        var id = UUID.randomUUID();
        var request = CouponCreateRequestMock.buildGoodValues();
        var coupon = Coupon.create(request.code(), request.description(), request.discountValue(),
                request.expirationDate(), request.published());
        when(couponRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(coupon));

        couponService.delete(id);


        assertEquals(Status.DELETED, coupon.getStatus());
        verify(eventPublisher).publishEvent(any(CouponDeletedEvent.class));
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    public void shouldThrowException_whenCouponDoesNotExist_whenDelete() {
        var id = UUID.randomUUID();
        doThrow(new CouponIdNotFoundException("Coupon not found")).when(couponRepository).findByIdAndDeletedAtIsNull(id);
        assertThrows(CouponIdNotFoundException.class, () -> couponService.delete(id));

    }
}
