package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;

public class AdminCreateBusForm {
    private String name;
    private String licensePlate;
    private BusType busType;
    private BusCapacity busCapacity;
}
