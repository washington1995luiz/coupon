package br.com.washington.coupon.event;

import br.com.washington.coupon.event.dto.CouponAlreadyExistsEvent;
import br.com.washington.coupon.event.dto.CouponCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponCreatedListener {

    @EventListener
    public void onCreatedHandler(CouponCreatedEvent event) {
        log.info("event=coupon_created id={} code={}", event.id(), event.code());
    }


    @EventListener
    public void onAlreadyExistsHandler(CouponAlreadyExistsEvent event) {
        log.info("event=coupon_already_exists code={}", event.code());
    }
}
