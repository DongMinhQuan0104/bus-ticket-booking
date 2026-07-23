package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.Min;

import java.util.UUID;

public class AdminCreateRouteStationForm {

    private UUID id;

    @Min(value = 0, message = "station order can not negative")
    private Integer stationOrder;

    @Min(value = 0, message = "price can not negative")
    private Double priceFromStart;

    public AdminCreateRouteStationForm() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getStationOrder() {
        return stationOrder;
    }

    public void setStationOrder(Integer stationOrder) {
        this.stationOrder = stationOrder;
    }

    public Double getPriceFromStart() {
        return priceFromStart;
    }

    public void setPriceFromStart(Double priceFromStart) {
        this.priceFromStart = priceFromStart;
    }
}
