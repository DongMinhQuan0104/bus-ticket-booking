package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Yeu cau hoan tien cho Admin duyet. Sinh tu Transaction khi khach huy ve.
 * pending = true khi trang thai PENDING (chua duyet) -> hien nut Duyet.
 */
public record RefundRequestViewModel(
        UUID id,
        String customerName,
        String customerEmail,
        Double amount,
        LocalDateTime createdAt,
        String status,
        boolean pending
) {}
