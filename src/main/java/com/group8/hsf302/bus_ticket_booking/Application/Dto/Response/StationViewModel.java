package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.UUID;

public record StationViewModel(
        UUID id,
        String name,
        String address,
        Status status
) {}