package br.com.washington.coupon.event;

import br.com.washington.coupon.event.dto.CouponDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponDeletedListener {

    @EventListener
    public void onDeleteHandler(CouponDeletedEvent event) {
        log.info("event=coupon_deleted id={} code={}", event.id(), event.code());
    }

}
