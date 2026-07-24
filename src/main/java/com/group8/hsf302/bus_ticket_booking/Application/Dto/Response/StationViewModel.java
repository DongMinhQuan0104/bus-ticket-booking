package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.UUID;

/**
 * Du lieu 1 tram (Station) tra ra giao dien Admin.
 * (Truoc day record rong () nen FE khong co field de hien - da bo sung day du.)
 */
public record StationViewModel(
        UUID id,
        String name,
        String address,
        Status status
) {}
