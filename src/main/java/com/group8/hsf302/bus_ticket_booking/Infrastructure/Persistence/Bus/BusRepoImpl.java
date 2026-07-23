package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Bus;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BusRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BusRepoImpl implements BusRepo {

    @Override
    public Optional<Bus> findByLicensePlate(String licensePlate) {
        return Optional.empty();
    }
}
