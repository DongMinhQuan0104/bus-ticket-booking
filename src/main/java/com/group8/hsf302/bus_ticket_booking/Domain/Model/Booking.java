package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "`booking`")
public class Booking {
    @Id
    private UUID id;

    private LocalDateTime dateBooked;

    private Double totalPrice;

    private String note;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
