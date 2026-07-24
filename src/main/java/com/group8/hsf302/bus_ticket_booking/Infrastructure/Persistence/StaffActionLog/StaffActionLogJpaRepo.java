package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.StaffActionLog;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.StaffActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffActionLogJpaRepo
        extends JpaRepository<StaffActionLog, UUID> {
}