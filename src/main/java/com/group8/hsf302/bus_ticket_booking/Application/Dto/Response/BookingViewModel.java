package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingViewModel(
        UUID id,
        LocalDateTime dateBooked,
        Double totalPrice,
        BookingType bookingType,
        String note,
        UUID tripId,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String busName,
        String driverName,
        List<String> seatCodes,
        List<String> passengerNames,
        boolean completed
) {
    // Chuyen da khoi hanh -> co the danh gia (E6); chua khoi hanh -> co the huy (E5)
    public boolean canReview() {
        return completed;
    }

    public boolean canCancel() {
        return !completed;
    }
}
