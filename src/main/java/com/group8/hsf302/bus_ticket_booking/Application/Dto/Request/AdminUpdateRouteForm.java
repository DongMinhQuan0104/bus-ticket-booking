package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AdminUpdateRouteForm {

    @NotBlank(message = "name can not blank")
    private String name;

    @Valid
    private List<AdminRouteStationForm> stations;

    public AdminUpdateRouteForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdminRouteStationForm> getStations() {
        return stations;
    }

    public void setStations(List<AdminRouteStationForm> stations) {
        this.stations = stations;
    }
}
