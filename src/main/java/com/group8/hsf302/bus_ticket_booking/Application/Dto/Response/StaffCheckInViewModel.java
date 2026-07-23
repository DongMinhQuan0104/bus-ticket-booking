package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TicketStatus;

import java.time.LocalDateTime;

public record StaffCheckInViewModel(
        String ticketCode,
        TicketStatus status,
        LocalDateTime checkedInAt,
        String passengerName,
        String seatCode,
        String message
) {
}