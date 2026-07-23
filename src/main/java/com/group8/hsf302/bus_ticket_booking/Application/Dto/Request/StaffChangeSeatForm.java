package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StaffChangeSeatForm(
        @NotNull UUID bookingDetailId,
        @NotBlank String newSeatCode
) {
}