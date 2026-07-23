package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record StaffSupportRequestForm(
        UUID bookingId,
        @NotBlank String subject,
        @NotBlank String description,
        boolean escalateToAdmin
) {
}