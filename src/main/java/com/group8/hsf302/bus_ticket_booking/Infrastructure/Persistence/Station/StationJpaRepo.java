package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Station;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StationJpaRepo extends JpaRepository<Station, UUID> {
}
