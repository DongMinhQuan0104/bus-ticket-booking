package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "booking_detail")
public class BookingDetail {

    @Id
    private UUID id;

    private String passengerName;

    private Double ticketPrice;

    private Double luggageWeightKg;

    private Double luggageFee;

    private Double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
