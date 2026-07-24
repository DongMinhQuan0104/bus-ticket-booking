package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository (interface o tang Domain) cho Trip. Cai dat that o Infrastructure (TripRepoImpl).
 * searchAvailable: E1 (tim chuyen con dat duoc). findById: E2/E3.
 * findByDriverName/save: nghiep vu Driver (An). findAll/delete/findByRouteName...: Admin (B4 - Quan).
 */
public interface TripRepo {
    // ===== Customer (Viet) =====
    List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                               LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Trip> findById(UUID id);

    // ===== Driver (An) =====
    List<Trip> findByDriverName(String driverName);

    Trip save(Trip trip);

    // ===== Admin (B4 - quan ly chuyen) =====
    Page<Trip> findAll(Pageable pageable);

    Page<Trip> findByRouteNameContainingIgnoreCase(String name, Pageable pageable);

    // Dashboard: cac chuyen "dang hoat dong" (SCHEDULED hoac RUNNING).
    Page<Trip> findActiveTrips(Pageable pageable);

    long countActiveTrips();

    void delete(Trip trip);

    // ===== Goi y thanh pho cho o tim kiem (E1) =====
    List<String> findDistinctDepartureCities();

    List<String> findDistinctArrivalCities();
}
