package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public record StaffTripViewModel(
        UUID tripId,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String routeName,
        String busName,
        String licensePlate,
        long availableSeatCount
) {
}