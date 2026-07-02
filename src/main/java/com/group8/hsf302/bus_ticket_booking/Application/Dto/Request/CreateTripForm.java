package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateTripForm {

    @NotBlank(message = "Destination from can not blank")
    private String destinationFrom;

    @NotBlank(message = "Destination to can not blank")
    private String destinationTo;

    @NotNull(message = "Departure time can not blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time can not blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalTime;

    @NotBlank(message = "Bus code can not blank")
    private String busCode;

    @NotBlank(message = "License plate can not blank")
    private String busLicensePlate;

    @NotBlank(message = "Vehicle type can not blank")
    private String vehicleType;

    @NotBlank(message = "Driver name can not blank")
    private String driverName;

    @NotNull(message = "Price can not blank")
    @DecimalMin(value = "0.0", message = "Price can not negative")
    private Double price;

    public CreateTripForm() {
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
}