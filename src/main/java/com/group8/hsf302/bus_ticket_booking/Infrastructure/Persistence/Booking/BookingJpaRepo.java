package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingJpaRepo
        extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByBookingCodeIgnoreCase(
            String bookingCode
    );

    @Query("""
            select b
            from Booking b
            where lower(b.bookingCode)
                    like concat('%', concat(lower(:keyword), '%'))
               or lower(b.contactPhone)
                    like concat('%', concat(lower(:keyword), '%'))
               or lower(b.contactName)
                    like concat('%', concat(lower(:keyword), '%'))
            order by b.dateBooked desc
            """)
    List<Booking> search(
            @Param("keyword") String keyword
    );

    List<Booking> findByStatusAndExpiresAtLessThanEqual(
            BookingStatus status,
            LocalDateTime now
    );
}