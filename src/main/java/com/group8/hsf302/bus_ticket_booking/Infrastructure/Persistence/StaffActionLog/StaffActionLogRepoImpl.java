package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.StaffActionLog;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.StaffActionLog;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.StaffActionLogRepo;
import org.springframework.stereotype.Repository;

@Repository
public class StaffActionLogRepoImpl
        implements StaffActionLogRepo {

    private final StaffActionLogJpaRepo jpaRepo;

    public StaffActionLogRepoImpl(
            StaffActionLogJpaRepo jpaRepo
    ) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public StaffActionLog save(
            StaffActionLog log
    ) {
        return jpaRepo.save(log);
    }
}