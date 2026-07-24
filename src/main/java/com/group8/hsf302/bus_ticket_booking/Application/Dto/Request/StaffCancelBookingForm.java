package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public record StaffCancelBookingForm(
        @NotBlank String reason,
        boolean requestRefund
) {
}