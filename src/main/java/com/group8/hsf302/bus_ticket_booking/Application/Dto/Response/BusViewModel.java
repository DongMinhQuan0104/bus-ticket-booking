package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.UUID;

public record BusViewModel (
        UUID id,
        String busName,
        String licensePlate,
        BusType busType,
        BusCapacity busCapacity,
        Status status
){}

