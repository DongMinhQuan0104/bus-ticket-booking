package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.UUID;

public record AccountViewModel(
        UUID id,
        String fullName,
        Role role,
        String email,
        String phoneNumber,
        Status status
) {}
