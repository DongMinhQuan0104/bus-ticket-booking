package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.TripMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccountNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.OldPasswordNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final AccountRepo accountRepo;
    private final AccountMapper mapper;
    private final PasswordHasher passwordHasher;
    private final TripRepo tripRepo;
    private final SeatAvailabilityRepo seatAvailabilityRepo;
    private final TripMapper tripMapper;

    public CustomerServiceImpl(AccountRepo accountRepo, AccountMapper mapper, PasswordHasher passwordHasher,
                               TripRepo tripRepo, SeatAvailabilityRepo seatAvailabilityRepo, TripMapper tripMapper) {
        this.accountRepo = accountRepo;
        this.mapper = mapper;
        this.passwordHasher = passwordHasher;
        this.tripRepo = tripRepo;
        this.seatAvailabilityRepo = seatAvailabilityRepo;
        this.tripMapper = tripMapper;
    }

    @Override
    public AccountViewModel getAccount(UUID accountId) {
        Account account = findActiveById(accountId);
        return mapper.toViewModel(account);
    }

    @Override
    public AccountViewModel update(UpdateAccountForm form,UUID accountId) {
        Account oldAccount = findActiveById(accountId);
        Account updateAccount = mapper.updateEntityFromForm(form, oldAccount);
        accountRepo.save(updateAccount);
        return mapper.toViewModel(updateAccount);
    }

    @Override
    public boolean changePassword(ChangePasswordForm form,UUID accountId) {
        Account account = findActiveById(accountId);
        if(!passwordHasher.verify(form.getOldPassword(), account.getPassword())) {
            throw new OldPasswordNotMatchException();
        }
        if(!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            throw new PasswordConfirmNotMatchException();
        }
        String newPassword = passwordHasher.hash(form.getNewPassword());
        account.setPassword(newPassword);
        accountRepo.save(account);
        return true;
    }

    @Override
    public boolean deleted(UUID accountId) {
        Account account = findActiveById(accountId);
        account.setStatus(Status.NOT_AVAILABLE);
        accountRepo.save(account);
        return true;
    }

    private Account findActiveById(UUID accountId) {
        return accountRepo.findActiveById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripViewModel> searchTrips(SearchTripForm form) {
        String from = form.getDestinationFrom().trim();
        String to = form.getDestinationTo().trim();

        // BE khong tin FE: kiem tra lai nghiep vu tai tang service
        if (from.equalsIgnoreCase(to)) {
            throw new SameStationException();
        }

        LocalDate date = form.getDepartureDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Trip> trips = tripRepo.searchAvailable(from, to, startOfDay, endOfDay);

        List<TripViewModel> result = new ArrayList<>();
        for (Trip trip : trips) {
            int totalSeats = totalSeatsOf(trip);
            long booked = seatAvailabilityRepo.countBookedSeats(trip.getId());
            int available = totalSeats - (int) booked;
            if (available < 0) {
                available = 0;
            }
            result.add(tripMapper.toViewModel(trip, totalSeats, available));
        }
        return result;
    }

    private int totalSeatsOf(Trip trip) {
        if (trip.getBus() == null || trip.getBus().getCapacity() == null) {
            return 0;
        }
        BusCapacity capacity = trip.getBus().getCapacity();
        if (capacity == BusCapacity.SEAT_16) {
            return 16;
        }
        return 0;
    }
}
