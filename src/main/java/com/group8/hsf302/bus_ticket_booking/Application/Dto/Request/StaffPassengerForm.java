package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StaffPassengerForm(
        @NotBlank String passengerName,
        @NotBlank String seatCode,
        @Min(0) Double luggageWeightKg
) {
}