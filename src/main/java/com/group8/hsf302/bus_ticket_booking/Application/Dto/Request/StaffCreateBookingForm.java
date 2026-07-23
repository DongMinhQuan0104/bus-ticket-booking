package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record StaffCreateBookingForm(
        @NotNull UUID tripId,
        @NotNull Integer pickupStationOrder,
        @NotNull Integer dropoffStationOrder,
        @NotBlank String contactName,
        @NotBlank String contactPhone,
        @Email String contactEmail,
        String note,
        @NotNull BookingType bookingType,
        @NotEmpty List<@Valid StaffPassengerForm> passengers
) {
}