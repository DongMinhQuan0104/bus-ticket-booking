package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    private UUID id;

    private String destinationFrom;

    private String destinationTo;

    private LocalDateTime departureTime;

    private String busLicensePlate;

    private String driverName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

}
