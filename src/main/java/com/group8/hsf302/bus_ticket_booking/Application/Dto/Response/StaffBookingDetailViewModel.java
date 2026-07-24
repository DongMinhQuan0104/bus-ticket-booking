package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TicketStatus;

import java.util.UUID;

public record StaffBookingDetailViewModel(
        UUID bookingDetailId,
        String passengerName,
        String seatCode,
        Double ticketPrice,
        Double luggageFee,
        Double subTotal,
        Integer pickupStationOrder,
        Integer dropoffStationOrder,
        String ticketCode,
        TicketStatus ticketStatus
) {
}