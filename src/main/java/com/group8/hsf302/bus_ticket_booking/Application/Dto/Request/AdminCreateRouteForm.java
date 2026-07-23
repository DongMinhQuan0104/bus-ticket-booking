package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AdminCreateRouteForm {

    @NotBlank(message = "name can not blank")
    private String name;

    @Valid
    @NotEmpty(message = "at least two station")
    private List<AdminRouteStationForm> stations;

    public AdminCreateRouteForm() {
    }

    public List<AdminRouteStationForm> getStations() {
        return stations;
    }

    public void setStations(List<AdminRouteStationForm> stations) {
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
