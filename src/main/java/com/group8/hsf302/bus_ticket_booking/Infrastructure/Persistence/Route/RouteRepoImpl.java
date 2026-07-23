package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Route;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Route;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.RouteRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RouteRepoImpl implements RouteRepo {

    private final RouteJpaRepo routeJpaRepo;

    public RouteRepoImpl(RouteJpaRepo routeJpaRepo) {
        this.routeJpaRepo = routeJpaRepo;
    }

    @Override
    public Optional<Route> findByName(String name) {
        return routeJpaRepo.findByName(name);
    }

    @Override
    public void save(Route route) {
        routeJpaRepo.save(route);
    }

    @Override
    public Page<Route> findAll(Pageable pageable) {
        return routeJpaRepo.findAll(pageable);
    }

    @Override
    public Page<Route> findByNameContaining(String name, Pageable pageable) {
        return routeJpaRepo.findByNameContaining(name,pageable);
    }

    @Override
    public Optional<Route> findById(UUID id) {
        return routeJpaRepo.findById(id);
    }

    @Override
    public void delete(Route route) {
        routeJpaRepo.delete(route);
    }
}
