package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createPayment;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
