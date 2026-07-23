package com.group8.hsf302.bus_ticket_booking.Application.Service.Admin;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.Paging.PagedResponse;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.*;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService{

    private final AccountRepo accountRepo;
    private final BusRepo busRepo;
    private final RouteRepo routeRepo;
    private final StationRepo stationRepo;
    private final TripRepo tripRepo;
    private final PasswordHasher passwordHasher;
    private final AccountMapper accountMapper;
    private final BusMapper busMapper;
    private final RouteMapper routeMapper;
    private final StationMapper stationMapper;
    private final TripMapper tripMapper;

    public AdminServiceImpl(AccountRepo accountRepo, BusRepo busRepo, RouteRepo routeRepo, StationRepo stationRepo, TripRepo tripRepo, PasswordHasher passwordHasher, AccountMapper accountMapper, BusMapper busMapper, RouteMapper routeMapper, StationMapper stationMapper, TripMapper tripMapper) {
        this.accountRepo = accountRepo;
        this.busRepo = busRepo;
        this.routeRepo = routeRepo;
        this.stationRepo = stationRepo;
        this.tripRepo = tripRepo;
        this.passwordHasher = passwordHasher;
        this.accountMapper = accountMapper;
        this.busMapper = busMapper;
        this.routeMapper = routeMapper;
        this.stationMapper = stationMapper;
        this.tripMapper = tripMapper;
    }

    @Override
    public AccountViewModel createAccount(AdminCreateAccountForm form) {
        if(accountRepo.findByEmail(form.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        Account account = accountMapper.toEntity(form);
        String securedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(securedPassword);
        accountRepo.save(account);
        return accountMapper.toViewModel(account);
    }

    @Override
    @Transactional
    public PagedResponse<AccountViewModel> getAllAccounts(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> accountPage = accountRepo.findAll(pageRequest);
        List<AccountViewModel> accountViewModels = accountPage.stream()
                .map(accountMapper::toViewModel)
                .toList();

        return new PagedResponse<>(
                accountViewModels,
                accountPage.getNumber(),
                accountPage.getSize(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                accountPage.isLast()
        );
    }

    @Override
    @Transactional
    public PagedResponse<AccountViewModel> getAccountByName(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> accountPage = accountRepo.findByFullNameContaining(name, pageRequest);

        List<AccountViewModel> accountViewModels = accountPage.stream()
                .map(accountMapper::toViewModel)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                accountViewModels,
                accountPage.getNumber(),
                accountPage.getSize(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                accountPage.isLast()
        );
    }

    @Override
    public AccountViewModel getAccountById(UUID id) {
        Account account = findAccountById(id);
        return accountMapper.toViewModel(account);
    }

    @Override
    public boolean updateAccount(AdminUpdateAccountForm form, UUID accountId) {
        Account account = findAccountById(accountId);
        Account updateAccount = accountMapper.updateEntityFromForm(form, account);
        String securedPassword = passwordHasher.hash(updateAccount.getPassword());
        updateAccount.setPassword(securedPassword);
        accountRepo.save(updateAccount);
        return true;
    }

    @Override
    public boolean deleteAccount(UUID accountId) {
        Account account = findAccountById(accountId);
        accountRepo.delete(account);
        return true;
    }

    @Override
    public BusViewModel createBus(AdminCreateBusForm form) {
        if(existsBusByLicensePlate(form.getLicensePlate())){
            throw new LicensePlateAlreadyExistsException();
        }
        Bus bus = busMapper.toEntity(form);
        busRepo.save(bus);
        return busMapper.toViewModel(bus);
    }

    @Override
    public PagedResponse<BusViewModel> getAllBuses(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Bus> busPage = busRepo.findAll(pageRequest);
        List<BusViewModel> busViewModels = busPage.stream()
                .map(busMapper::toViewModel)
                .toList();

        return new PagedResponse<>(
                busViewModels,
                busPage.getNumber(),
                busPage.getSize(),
                busPage.getTotalElements(),
                busPage.getTotalPages(),
                busPage.isLast()
        );
    }

    @Override
    public PagedResponse<BusViewModel> getBusByName(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Bus> busPage = busRepo.findByBusNameContaining(name,pageRequest);
        List<BusViewModel> busViewModels = busPage.stream()
                .map(busMapper::toViewModel)
                .toList();

        return new PagedResponse<>(
                busViewModels,
                busPage.getNumber(),
                busPage.getSize(),
                busPage.getTotalElements(),
                busPage.getTotalPages(),
                busPage.isLast()
        );
    }

    @Override
    public BusViewModel getBusById(UUID id) {
        Bus bus = findBusById(id);
        return busMapper.toViewModel(bus);
    }

    @Override
    public boolean updateBus(AdminUpdateBusForm form, UUID id) {
        Bus bus = findBusById(id);
        Bus updateBus = busMapper.updateEntityFromForm(form,bus);
        busRepo.save(updateBus);
        return true;
    }

    @Override
    public boolean deleteBus(UUID id) {
        Bus bus = findBusById(id);
        busRepo.delete(bus);
        return true;
    }

    @Override
    public RouteViewModel createRoute(AdminCreateRouteForm form) {
        if(existsRouteByName(form.getName())){
            throw new RouteAlreadyExistsException();
        }
        Route route = routeMapper.toEntity(form);
        if (form.getStations() != null) {
            processRouteStations(form.getStations(), route);
        }
        routeRepo.save(route);
        return routeMapper.toViewModel(route);
    }

    @Override
    @Transactional
    public PagedResponse<RouteViewModel> getAllRoutes(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Route> routePage = routeRepo.findAll(pageRequest);
        List<RouteViewModel> routeViewModels = routePage.stream()
                .map(routeMapper::toViewModel)
                .toList();
        return new PagedResponse<>(
                routeViewModels,
                routePage.getNumber(),
                routePage.getSize(),
                routePage.getTotalElements(),
                routePage.getTotalPages(),
                routePage.isLast()
        );
    }

    @Override
    @Transactional
    public PagedResponse<RouteViewModel> getRouteByName(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Route> routePage = routeRepo.findByNameContaining(name, pageRequest);
        List<RouteViewModel> routeViewModels = routePage.stream()
                .map(routeMapper::toViewModel)
                .toList();

        return new PagedResponse<>(
                routeViewModels,
                routePage.getNumber(),
                routePage.getSize(),
                routePage.getTotalElements(),
                routePage.getTotalPages(),
                routePage.isLast()
        );
    }

    @Override
    public RouteViewModel getRouteById(UUID id) {
        Route route = findRouteById(id);
        return routeMapper.toViewModel(route);
    }

    @Override
    public boolean updateRoute(AdminUpdateRouteForm form, UUID id) {
        Route route = findRouteById(id);
        Route updateRoute = routeMapper.updateEntityFromForm(form,route);
        if (form.getStations() != null) {
            route.getRouteStations().clear();
            processRouteStations(form.getStations(), route);
        }
        routeRepo.save(updateRoute);
        return true;
    }

    @Override
    public boolean deletedRoute(UUID id) {
        Route route = findRouteById(id);
        routeRepo.delete(route);
        return true;
    }

    @Override
    public SeatAvailabilityViewModel createSeatAvailability(AdminCreateSeatAvailabilityForm form) {
        return null;
    }

    @Override
    public boolean updateSeatAvailability(AdminUpdateSeatAvailabilityForm form, UUID id) {
        return false;
    }

    @Override
    public boolean deletedSeat(UUID id) {
        return false;
    }

    @Override
    public StationViewModel createStation(AdminCreateStationForm form) {
        if(existStationByName(form.getName())){
            throw new StationNotFoundException();
        }
        Station station = stationMapper.toEntity(form);
        stationRepo.save(station);
        return stationMapper.toViewModel(station);
    }

    @Override
    @Transactional
    public PagedResponse<StationViewModel> getAllStations(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Station> stationPage = stationRepo.findAll(pageRequest);
        List<StationViewModel> stationViewModels = stationPage.stream()
                .map(stationMapper::toViewModel)
                .toList();
        return new PagedResponse<>(
                stationViewModels,
                stationPage.getNumber(),
                stationPage.getSize(),
                stationPage.getTotalElements(),
                stationPage.getTotalPages(),
                stationPage.isLast()
        );
    }

    @Override
    public PagedResponse<StationViewModel> getStationByName(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Station> stationPage = stationRepo.findByNameContaining(name,pageRequest);
        List<StationViewModel> stationViewModels = stationPage.stream()
                .map(stationMapper::toViewModel)
                .toList();
        return new PagedResponse<>(
                stationViewModels,
                stationPage.getNumber(),
                stationPage.getSize(),
                stationPage.getTotalElements(),
                stationPage.getTotalPages(),
                stationPage.isLast()
        );
    }

    @Override
    public StationViewModel getStationById(UUID id) {
        Station station = findStationById(id);
        return stationMapper.toViewModel(station);
    }

    @Override
    public boolean updateStation(AdminUpdateStationForm form, UUID id) {
        Station station = findStationById(id);
        Station updateStation = stationMapper.updateEntityFromForm(form,station);
        stationRepo.save(updateStation);
        return true;
    }

    @Override
    public boolean deletedStation(UUID id) {
        Station station = findStationById(id);
        stationRepo.delete(station);
        return true;
    }

    // ===================== TRIP (B4 - Admin quan ly chuyen) =====================
    // Truoc day cac method nay tra ve null (chua cai). Da hoan thien de Admin FE dung duoc.

    @Override
    public TripViewModel createTrip(AdminCreateTripForm form) {
        Trip trip = new Trip();
        applyTripForm(trip, form.getDestinationFrom(), form.getDestinationTo(), form.getDepartureTime(),
                form.getDriverName(), form.getPrice(), form.getRouteId(), form.getBusId(),
                form.getStatus() != null ? form.getStatus() : TripStatus.SCHEDULED);
        Trip saved = tripRepo.save(trip);
        return tripMapper.toViewModel(saved, totalSeatsOfTrip(saved), totalSeatsOfTrip(saved));
    }

    @Override
    public PagedResponse<TripViewModel> getAllTrips(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "departureTime"));
        Page<Trip> tripPage = tripRepo.findAll(pageRequest);
        List<TripViewModel> viewModels = tripPage.stream()
                .map(t -> tripMapper.toViewModel(t, totalSeatsOfTrip(t), totalSeatsOfTrip(t)))
                .toList();
        return new PagedResponse<>(
                viewModels, tripPage.getNumber(), tripPage.getSize(),
                tripPage.getTotalElements(), tripPage.getTotalPages(), tripPage.isLast());
    }

    @Override
    public PagedResponse<TripViewModel> getTripByName(String name, int page, int size) {
        // Tam thoi khong loc theo ten (Trip khong co "ten"); tra ve toan bo co phan trang.
        return getAllTrips(page, size);
    }

    @Override
    public PagedResponse<TripViewModel> getTripById(UUID id, int page, int size) {
        List<TripViewModel> content = tripRepo.findById(id)
                .map(t -> List.of(tripMapper.toViewModel(t, totalSeatsOfTrip(t), totalSeatsOfTrip(t))))
                .orElse(List.of());
        return new PagedResponse<>(content, 0, size, content.size(), content.isEmpty() ? 0 : 1, true);
    }

    @Override
    public boolean updateTrip(AdminUpdateTripForm form, UUID id) {
        return tripRepo.findById(id).map(trip -> {
            applyTripForm(trip, form.getDestinationFrom(), form.getDestinationTo(), form.getDepartureTime(),
                    form.getDriverName(), form.getPrice(), form.getRouteId(), form.getBusId(), form.getStatus());
            tripRepo.save(trip);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean deletedTrip(UUID id) {
        if (tripRepo.findById(id).isEmpty()) {
            return false;
        }
        tripRepo.deleteById(id);
        return true;
    }

    /** Gan cac truong tu form vao Trip (chi ghi de khi gia tri khong null - dung chung create/update). */
    private void applyTripForm(Trip trip, String from, String to, java.time.LocalDateTime departure,
                               String driverName, Double price, UUID routeId, UUID busId, TripStatus status) {
        if (from != null) trip.setDestinationFrom(from);
        if (to != null) trip.setDestinationTo(to);
        if (departure != null) trip.setDepartureTime(departure);
        if (driverName != null) trip.setDriverName(driverName);
        if (price != null) trip.setPrice(price);
        if (status != null) trip.setStatus(status);
        if (routeId != null) routeRepo.findById(routeId).ifPresent(trip::setRoute);
        if (busId != null) busRepo.findById(busId).ifPresent(trip::setBus);
    }

    /** Tong so ghe theo suc chua cua xe (0 neu chua gan xe). */
    private int totalSeatsOfTrip(Trip trip) {
        if (trip.getBus() == null || trip.getBus().getCapacity() == null) return 0;
        return trip.getBus().getCapacity().getSeats();
    }



    private Account findAccountById(UUID accountId) {
        return accountRepo.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    private Bus findBusById(UUID id){
        return busRepo.findById(id).orElseThrow(BusNotFoundException::new);
    }

    private boolean existsBusByLicensePlate(String licensePlate){
        return busRepo.findByLicensePlate(licensePlate).isPresent();
    }

    private boolean existsRouteByName(String name){
        return routeRepo.findByName(name).isPresent();
    }

    private Route findRouteById(UUID id){
        return routeRepo.findById(id).orElseThrow(RouteAlreadyExistsException::new);
    }

    private boolean existStationByName(String name){
        return stationRepo.findByName(name).isPresent();
    }

    private Station findStationById(UUID id){
        return stationRepo.findById(id).orElseThrow(StationNotFoundException::new);
    }

    private void processRouteStations(List<AdminRouteStationForm> stationForms, Route parentRoute) {
        for (AdminRouteStationForm stationForm : stationForms) {
            RouteStation routeStation = routeMapper.toRouteStationEntity(stationForm);
            Station station = findStationById(stationForm.getStationId());
            routeStation.setStation(station);
            routeStation.setRoute(parentRoute);
            parentRoute.getRouteStations().add(routeStation);
        }
    }
}
