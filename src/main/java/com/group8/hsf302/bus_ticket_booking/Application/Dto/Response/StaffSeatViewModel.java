package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;

import java.time.LocalDateTime;

public record StaffSeatViewModel(
        String seatCode,
        SeatStatus status,
        LocalDateTime holdExpiredAt
) {
}