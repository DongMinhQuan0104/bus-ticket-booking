package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    public AccountViewModel getAccount(UUID accountId);
    public AccountViewModel update(UpdateAccountForm form,UUID  accountId);
    public boolean changePassword(ChangePasswordForm form,UUID accountId);
    public boolean deleted(UUID accountId);

    // E1 - Tim kiem chuyen xe
    List<TripViewModel> searchTrips(SearchTripForm form);

    // E2 - Chon ghe & dat ve
    TripViewModel getTripForBooking(UUID tripId);
    List<String> getOccupiedSeatCodes(UUID tripId);

    // E3 - Tao booking & thanh toan
    UUID createBooking(CreateBookingForm form, UUID accountId);

    // E4 - Ve cua toi
    List<BookingViewModel> getMyBookings(UUID accountId);
}
