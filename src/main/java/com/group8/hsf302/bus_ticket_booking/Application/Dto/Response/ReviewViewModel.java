package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Feedback cua khach hang de Admin xem (danh gia chuyen di - E6).
 */
public record ReviewViewModel(
        UUID id,
        Integer rating,
        String comment,
        String customerName,
        String tripName,
        LocalDateTime createdAt
) {}
