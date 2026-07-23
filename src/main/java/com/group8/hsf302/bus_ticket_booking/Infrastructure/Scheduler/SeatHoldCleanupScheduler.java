package com.group8.hsf302.bus_ticket_booking.Infrastructure.Scheduler;

import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tu dong giai phong cac ghe GIU TAM da qua han (khach vao thanh toan nhung khong hoan tat).
 * Chay moi 60 giay. Dung @Scheduled co san cua Spring - khong them cong nghe moi.
 * Xem CustomerServiceImpl.SEAT_HOLD_MINUTES de biet thoi gian giu ghe.
 */
@Component
public class SeatHoldCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(SeatHoldCleanupScheduler.class);

    private final CustomerService customerService;

    public SeatHoldCleanupScheduler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void releaseExpiredHolds() {
        int released = customerService.releaseExpiredSeatHolds();
        if (released > 0) {
            log.info("Da giai phong {} ghe giu tam qua han", released);
        }
    }
}
