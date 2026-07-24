package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Ticket;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketJpaRepo
        extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByTicketCodeIgnoreCase(
            String ticketCode
    );

    Optional<Ticket> findByBookingDetail_Id(
            UUID bookingDetailId
    );

    @Query("""
            select t
            from Ticket t
            join fetch t.bookingDetail d
            join fetch d.booking b
            where b.id = :bookingId
            order by t.createdAt asc
            """)
    List<Ticket> findByBookingId(
            @Param("bookingId") UUID bookingId
    );
}