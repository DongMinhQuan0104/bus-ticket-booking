package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class BookingRepoImpl implements BookingRepo {

    @Autowired
    private BookingJpaRepo bookingJpaRepo;

    @Override
    public Optional<Booking> findById(UUID id) {
        return bookingJpaRepo.findById(id);
    }
}
