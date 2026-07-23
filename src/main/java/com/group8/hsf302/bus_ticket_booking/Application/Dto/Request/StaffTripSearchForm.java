package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StaffTripSearchForm(
        @NotBlank String destinationFrom,
        @NotBlank String destinationTo,
        @NotNull LocalDate departureDate
) {
}