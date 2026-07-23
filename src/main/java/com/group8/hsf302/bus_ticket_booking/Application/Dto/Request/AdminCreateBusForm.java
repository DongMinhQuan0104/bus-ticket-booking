package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;

public class AdminCreateBusForm {
    private String name;
    private String licensePlate;
    private BusType busType;
    private BusCapacity busCapacity;

    public AdminCreateBusForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BusCapacity getBusCapacity() {
        return busCapacity;
    }

    public void setBusCapacity(BusCapacity busCapacity) {
        this.busCapacity = busCapacity;
    }
}
