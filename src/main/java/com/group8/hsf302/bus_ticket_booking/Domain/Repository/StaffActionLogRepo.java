package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.StaffActionLog;

public interface StaffActionLogRepo {
    StaffActionLog save(StaffActionLog log);
}