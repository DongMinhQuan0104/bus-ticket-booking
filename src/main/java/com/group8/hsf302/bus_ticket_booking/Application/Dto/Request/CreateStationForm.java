package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public class CreateStationForm {

    @NotBlank(message = "name can not blank")
    private String name;

    @NotBlank(message = "address can not blank")
    private String address;

    public CreateStationForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}