package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "route")
public class Route {

    @Id
    private UUID id;

    private String name;
}
