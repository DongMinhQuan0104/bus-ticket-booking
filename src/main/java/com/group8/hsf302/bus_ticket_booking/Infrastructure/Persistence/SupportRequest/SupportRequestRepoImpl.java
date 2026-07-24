package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SupportRequest;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SupportRequest;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SupportRequestRepo;
import org.springframework.stereotype.Repository;

@Repository
public class SupportRequestRepoImpl
        implements SupportRequestRepo {

    private final SupportRequestJpaRepo jpaRepo;

    public SupportRequestRepoImpl(
            SupportRequestJpaRepo jpaRepo
    ) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public SupportRequest save(
            SupportRequest supportRequest
    ) {
        return jpaRepo.save(supportRequest);
    }
}