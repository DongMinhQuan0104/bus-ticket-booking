package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Bus;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BusRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BusRepoImpl implements BusRepo {

    private final BusJpaRepo busJpaRepo;

    public BusRepoImpl(BusJpaRepo busJpaRepo) {
        this.busJpaRepo = busJpaRepo;
    }

    @Override
    public Optional<Bus> findByLicensePlate(String licensePlate) {
        return busJpaRepo.findByLicensePlate(licensePlate);
    }

    @Override
    public void save(Bus bus) {
        busJpaRepo.save(bus);
    }

    @Override
    public Page<Bus> findAll(Pageable pageable) {
        return busJpaRepo.findAll(pageable);
    }

    @Override
    public Page<Bus> findByBusNameContaining(String name, Pageable pageable) {
        return busJpaRepo.findByBusNameContaining(name,pageable);
    }

    @Override
    public Optional<Bus> findById(UUID id) {
        return busJpaRepo.findById(id);
    }

    @Override
    public void delete(Bus bus) {
        busJpaRepo.delete(bus);
    }
}
