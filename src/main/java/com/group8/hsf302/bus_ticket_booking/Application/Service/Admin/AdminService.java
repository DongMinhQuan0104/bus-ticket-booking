package com.group8.hsf302.bus_ticket_booking.Application.Service.Admin;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.Paging.PagedResponse;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    public AccountViewModel createAccount(AdminCreateAccountForm form);
    public PagedResponse<AccountViewModel> getAllAccounts(int page, int size);
    public PagedResponse<AccountViewModel> getAccountByName(String name, int page, int size);
    public AccountViewModel getAccountById(UUID id);
    public List<AccountViewModel> getAccountByRole(Role role);
    public boolean updateAccount(AdminUpdateAccountForm form,UUID id);
    public boolean deleteAccount(UUID id);


    public BusViewModel createBus(AdminCreateBusForm form);
    public PagedResponse<BusViewModel> getAllBuses(int page, int size);
    public PagedResponse<BusViewModel> getBusByName(String name,int page, int size);
    public BusViewModel getBusById(UUID id);
    public boolean updateBus(AdminUpdateBusForm form, UUID id);
    public boolean deleteBus(UUID id);

    public RouteViewModel createRoute(AdminCreateRouteForm form);
    public PagedResponse<RouteViewModel> getAllRoutes(int page, int size);
    public PagedResponse<RouteViewModel> getRouteByName(String name, int page, int size);
    public RouteViewModel getRouteById(UUID id);
    public boolean updateRoute(AdminUpdateRouteForm form, UUID id);
    public boolean deletedRoute(UUID id);

    public StationViewModel createStation(AdminCreateStationForm form);
    public PagedResponse<StationViewModel> getAllStations(int page, int size);
    public PagedResponse<StationViewModel> getStationByName(String name, int page, int size);
    public StationViewModel getStationById(UUID id);
    public boolean updateStation(AdminUpdateStationForm form, UUID id);
    public boolean deletedStation(UUID id);

    public TripViewModel createTrip(AdminCreateTripForm form);
    public PagedResponse<TripViewModel> getAllTrips(int page, int size);
    public PagedResponse<TripViewModel> getTripByRouteName(String name, int page, int size);
    public TripViewModel getTripById(UUID id);
    public boolean updateTrip(AdminUpdateTripForm form, UUID id);
    public boolean deletedTrip(UUID id);

    public SeatAvailabilityViewModel createSeatAvailability(AdminCreateSeatAvailabilityForm form);
    public boolean updateSeatAvailability(AdminUpdateSeatAvailabilityForm form, UUID id);
    public boolean deletedSeat(UUID id);
}
