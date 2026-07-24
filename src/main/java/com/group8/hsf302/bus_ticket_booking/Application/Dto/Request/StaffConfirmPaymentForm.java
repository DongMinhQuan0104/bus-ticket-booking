package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record StaffConfirmPaymentForm(
        @NotNull PaymentMethod paymentMethod,
        String referenceCode
) {
}