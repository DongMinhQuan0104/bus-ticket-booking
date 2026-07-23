package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AdminRouteStationForm {

    @NotNull(message = "station id not null")
    private UUID stationId;

    @Min(value = 0, message = "station order can not negative")
    private Integer stationOrder;

    @Min(value = 0, message = "price can not negative")
    private Double priceFromStart;

    public AdminRouteStationForm() {
    }

    public UUID getStationId() {
        return stationId;
    }

    public void setStationId(UUID stationId) {
        this.stationId = stationId;
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
