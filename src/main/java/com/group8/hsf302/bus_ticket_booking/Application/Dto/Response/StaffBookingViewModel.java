package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StaffBookingViewModel(
        UUID bookingId,
        String bookingCode,
        LocalDateTime dateBooked,
        Double totalPrice,
        BookingStatus status,
        String contactName,
        String contactPhone,
        String contactEmail,
        UUID tripId,
        LocalDateTime departureTime,
        List<StaffBookingDetailViewModel> details
) {
}