package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class DriverTripViewModel {

    private UUID id;
    private String destinationFrom;
    private String destinationTo;
    private LocalDateTime departureTime;
    private String driverName;
    private TripStatus status;
    private String routeName;
    private String busName;
    private String busLicensePlate;
    private int totalPassengers;

    public DriverTripViewModel() {
    }

    public DriverTripViewModel(UUID id, String destinationFrom, String destinationTo, LocalDateTime departureTime, String driverName, TripStatus status, String routeName, String busName, String busLicensePlate, int totalPassengers) {
        this.id = id;
        this.destinationFrom = destinationFrom;
        this.destinationTo = destinationTo;
        this.departureTime = departureTime;
        this.driverName = driverName;
        this.status = status;
        this.routeName = routeName;
        this.busName = busName;
        this.busLicensePlate = busLicensePlate;
        this.totalPassengers = totalPassengers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDestinationFrom() {
        return destinationFrom;
    }

    public void setDestinationFrom(String destinationFrom) {
        this.destinationFrom = destinationFrom;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public void setDestinationTo(String destinationTo) {
        this.destinationTo = destinationTo;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusLicensePlate() {
        return busLicensePlate;
    }

    public void setBusLicensePlate(String busLicensePlate) {
        this.busLicensePlate = busLicensePlate;
    }

    public int getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }
}
