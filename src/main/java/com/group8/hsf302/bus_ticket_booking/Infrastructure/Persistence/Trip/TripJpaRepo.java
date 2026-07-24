package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {

    // ===== Customer (Viet) - E1: tim chuyen theo trang thai + diem di/den + khoang ngay di =====
    // Truong "status" cua Trip la kieu TripStatus (thong nhat 1 truong). Khach chi thay chuyen SCHEDULED.
    List<Trip> findByStatusAndDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenOrderByDepartureTimeAsc(
            TripStatus status, String destinationFrom, String destinationTo,
            LocalDateTime start, LocalDateTime end);

    // ===== Driver (An) =====
    List<Trip> findByDriverName(String driverName);

    // ===== Admin (B4 - Quan): tim chuyen theo ten tuyen =====
    Page<Trip> findByRouteNameContainingIgnoreCase(String name, Pageable pageable);

    // ===== Goi y thanh pho cho o tim kiem (FE hien datalist de khoi go tay) =====
    @Query("select distinct t.destinationFrom from Trip t order by t.destinationFrom")
    List<String> findDistinctDepartureCities();

    @Query("select distinct t.destinationTo from Trip t order by t.destinationTo")
    List<String> findDistinctArrivalCities();
}
