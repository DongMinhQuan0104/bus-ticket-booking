package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Ticket;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Ticket;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TicketRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TicketRepoImpl implements TicketRepo {

    private final TicketJpaRepo ticketJpaRepo;

    public TicketRepoImpl(
            TicketJpaRepo ticketJpaRepo
    ) {
        this.ticketJpaRepo = ticketJpaRepo;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketJpaRepo.save(ticket);
    }

    @Override
    public List<Ticket> saveAll(
            List<Ticket> tickets
    ) {
        return ticketJpaRepo.saveAll(tickets);
    }

    @Override
    public Optional<Ticket> findByTicketCode(
            String ticketCode
    ) {
        return ticketJpaRepo
                .findByTicketCodeIgnoreCase(ticketCode);
    }

    @Override
    public Optional<Ticket> findByBookingDetailId(
            UUID detailId
    ) {
        return ticketJpaRepo
                .findByBookingDetail_Id(detailId);
    }

    @Override
    public List<Ticket> findByBookingId(
            UUID bookingId
    ) {
        return ticketJpaRepo.findByBookingId(
                bookingId
        );
    }
}