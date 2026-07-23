package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AdminUpdateBusForm {

    private String busName;

    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private BusType busType;

    @Enumerated(EnumType.STRING)
    private BusCapacity capacity;

    @Enumerated(EnumType.STRING)
    private Status status;

    public AdminUpdateBusForm() {
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }

    public BusCapacity getCapacity() {
        return capacity;
    }

    public void setCapacity(BusCapacity capacity) {
        this.capacity = capacity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
