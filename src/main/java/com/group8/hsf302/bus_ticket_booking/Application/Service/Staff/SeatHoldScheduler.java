package com.group8.hsf302.bus_ticket_booking.Application.Service.Staff;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SeatHoldScheduler {

    private final StaffService staffService;

    public SeatHoldScheduler(
            StaffService staffService
    ) {
        this.staffService = staffService;
    }

    /*
     * Mặc định chạy mỗi 60 giây.
     * fixedDelay nghĩa là đợi 60 giây sau khi
     * lần chạy trước kết thúc.
     */
    @Scheduled(
            fixedDelayString =
                    "${staff.seat-hold-cleanup-ms:60000}"
    )
    public void releaseExpiredSeatHolds() {
        staffService.releaseExpiredHolds();
    }
}