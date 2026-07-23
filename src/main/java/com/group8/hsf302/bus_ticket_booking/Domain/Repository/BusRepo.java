package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;

import java.util.Optional;

public interface BusRepo {
    Optional<Bus> findByLicensePlate(String licensePlate);
}
