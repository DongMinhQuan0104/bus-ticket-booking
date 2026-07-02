package com.group8.hsf302.bus_ticket_booking.Application.Service.Station;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StationViewModel;

import java.util.List;
import java.util.UUID;

public interface StationService {

    StationViewModel create(CreateStationForm form);

    List<StationViewModel> getAll();

    StationViewModel getById(UUID id);

    StationViewModel update(UUID id, UpdateStationForm form);

    boolean delete(UUID id);
}