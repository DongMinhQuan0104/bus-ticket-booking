package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public class AdminCreateRouteForm {

    @NotBlank(message = "name can not blank")
    private String name;

    public AdminCreateRouteForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
