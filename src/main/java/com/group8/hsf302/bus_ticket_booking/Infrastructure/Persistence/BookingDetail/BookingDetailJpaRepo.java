package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.BookingDetail;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingDetailJpaRepo extends JpaRepository<BookingDetail, UUID> {
}
