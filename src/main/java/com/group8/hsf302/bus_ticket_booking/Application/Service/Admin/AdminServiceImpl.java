package com.group8.hsf302.bus_ticket_booking.Application.Service.Admin;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.Paging.PagedResponse;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.BusMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccountNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.BusNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.EmailAlreadyExistsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.LicensePlateAlreadyExistsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BusRepo;
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
    private final PasswordHasher passwordHasher;
    private final AccountMapper accountMapper;
    private final BusMapper busMapper;

    public AdminServiceImpl(AccountRepo accountRepo, BusRepo busRepo, PasswordHasher passwordHasher, AccountMapper accountMapper, BusMapper busMapper) {
        this.accountRepo = accountRepo;
        this.busRepo = busRepo;
        this.passwordHasher = passwordHasher;
        this.accountMapper = accountMapper;
        this.busMapper = busMapper;
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
        if(existsByLicensePlate(form.getLicensePlate())){
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
    public RouteStationViewModel createRouteStation(AdminCreateRouteStationForm form) {
        return null;
    }

    @Override
    public PagedResponse<RouteStationViewModel> getAllRouteStations(int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<RouteStationViewModel> getRouteStationByName(String name, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<RouteStationViewModel> getRouteStationById(UUID id, int page, int size) {
        return null;
    }

    @Override
    public boolean updateRouteStation(AdminUpdateRouteStationForm form, UUID id) {
        return false;
    }

    @Override
    public boolean deleteRouteStation(UUID id) {
        return false;
    }

    @Override
    public RouteViewModel createRoute(AdminCreateRouteForm form) {
        return null;
    }

    @Override
    public PagedResponse<RouteViewModel> getAllRoutes(int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<RouteViewModel> getRouteByName(String name, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<RouteViewModel> getRouteById(UUID id, int page, int size) {
        return null;
    }

    @Override
    public boolean updateRoute(AdminUpdateRouteForm form, UUID id) {
        return false;
    }

    @Override
    public boolean deletedRoute(UUID id) {
        return false;
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
        return null;
    }

    @Override
    public PagedResponse<StationViewModel> getAllStations(int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<StationViewModel> getStationByName(String name, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<StationViewModel> getStationById(UUID id, int page, int size) {
        return null;
    }

    @Override
    public boolean updateStation(AdminUpdateStationForm form, UUID id) {
        return false;
    }

    @Override
    public boolean deletedStation(UUID id) {
        return false;
    }

    @Override
    public TripViewModel createTrip(AdminCreateTripForm form) {
        return null;
    }

    @Override
    public PagedResponse<TripViewModel> getAllTrips(int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<TripViewModel> getTripByName(String name, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<TripViewModel> getTripById(UUID id, int page, int size) {
        return null;
    }

    @Override
    public boolean updateTrip(AdminUpdateTripForm form, UUID id) {
        return false;
    }

    @Override
    public boolean deletedTrip(UUID id) {
        return false;
    }

    private Account findAccountById(UUID accountId) {
        return accountRepo.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    private Bus findBusById(UUID id){
        return busRepo.findById(id).orElseThrow(BusNotFoundException::new);
    }

    private boolean existsByLicensePlate(String licensePlate){
        return busRepo.findByLicensePlate(licensePlate).isPresent();
    }


}
