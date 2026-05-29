package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "station")
public class Station {

    @Id
    private UUID id;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;
}
