package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;

import java.util.List;

public interface CustomerSearchTripService {
    List<TripViewModel> search(SearchTripForm form);
}
