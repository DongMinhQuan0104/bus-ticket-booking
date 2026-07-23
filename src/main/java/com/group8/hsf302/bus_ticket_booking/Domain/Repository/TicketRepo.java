package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepo {
    Ticket save(Ticket ticket);

    List<Ticket> saveAll(List<Ticket> tickets);

    Optional<Ticket> findByTicketCode(String ticketCode);

    Optional<Ticket> findByBookingDetailId(UUID detailId);

    List<Ticket> findByBookingId(UUID bookingId);
}