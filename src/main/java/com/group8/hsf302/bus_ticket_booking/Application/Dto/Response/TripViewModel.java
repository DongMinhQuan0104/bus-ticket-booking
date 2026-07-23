package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripViewModel(
        UUID id,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String driverName,
        Double price,
        String busName,
        BusType busType,
        BusCapacity busCapacity,
        int totalSeats,
        int availableSeats
) {
    public boolean soldOut() {
        return availableSeats <= 0;
    }
}
