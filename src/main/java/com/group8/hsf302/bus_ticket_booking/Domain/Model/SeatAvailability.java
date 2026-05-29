package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "seat_availability")
public class SeatAvailability {

    @Id
    private UUID id;

    private String seatCode;

    private Integer startStationOrder;

    private Integer endStationOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
