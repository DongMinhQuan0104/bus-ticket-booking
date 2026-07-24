package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripViewModel(
        UUID id,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String driverName,
        Status status,
        UUID routeId,
        String routeName,
        UUID busId,
        String busLicensePlate
) {
}
