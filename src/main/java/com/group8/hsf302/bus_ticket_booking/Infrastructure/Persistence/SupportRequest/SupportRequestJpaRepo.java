package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SupportRequest;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupportRequestJpaRepo
        extends JpaRepository<SupportRequest, UUID> {
}