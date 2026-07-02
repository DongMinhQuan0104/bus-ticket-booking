package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripViewModel {

    private UUID id;
    private String destinationFrom;
    private String destinationTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String busCode;
    private String busLicensePlate;
    private String vehicleType;
    private String driverName;
    private Double price;
    private Integer totalSeats;
    private Integer availableSeats;
    private TripStatus status;

    public TripViewModel() {
    }

    public TripViewModel(UUID id, String destinationFrom, String destinationTo,
                         LocalDateTime departureTime, LocalDateTime arrivalTime,
                         String busCode, String busLicensePlate, String vehicleType,
                         String driverName, Double price, Integer totalSeats,
                         Integer availableSeats, TripStatus status) {
        this.id = id;
        this.destinationFrom = destinationFrom;
        this.destinationTo = destinationTo;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.busCode = busCode;
        this.busLicensePlate = busLicensePlate;
        this.vehicleType = vehicleType;
        this.driverName = driverName;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getDestinationFrom() {
        return destinationFrom;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getBusCode() {
        return busCode;
    }

    public String getBusLicensePlate() {
        return busLicensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDriverName() {
        return driverName;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDestinationFrom(String destinationFrom) {
        this.destinationFrom = destinationFrom;
    }

    public void setDestinationTo(String destinationTo) {
        this.destinationTo = destinationTo;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public void setBusLicensePlate(String busLicensePlate) {
        this.busLicensePlate = busLicensePlate;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }
}