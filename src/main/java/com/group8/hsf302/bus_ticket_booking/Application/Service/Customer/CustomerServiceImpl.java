package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.TripMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccountNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.OldPasswordNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SeatAlreadyBookedException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.TripNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingDetailRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
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
    private final BookingRepo bookingRepo;
    private final BookingDetailRepo bookingDetailRepo;
    private final PaymentRepo paymentRepo;

    public CustomerServiceImpl(AccountRepo accountRepo, AccountMapper mapper, PasswordHasher passwordHasher,
                               TripRepo tripRepo, SeatAvailabilityRepo seatAvailabilityRepo, TripMapper tripMapper,
                               BookingRepo bookingRepo, BookingDetailRepo bookingDetailRepo, PaymentRepo paymentRepo) {
        this.accountRepo = accountRepo;
        this.mapper = mapper;
        this.passwordHasher = passwordHasher;
        this.tripRepo = tripRepo;
        this.seatAvailabilityRepo = seatAvailabilityRepo;
        this.tripMapper = tripMapper;
        this.bookingRepo = bookingRepo;
        this.bookingDetailRepo = bookingDetailRepo;
        this.paymentRepo = paymentRepo;
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

    @Override
    @Transactional(readOnly = true)
    public TripViewModel getTripForBooking(UUID tripId) {
        Trip trip = tripRepo.findById(tripId)
                .filter(t -> t.getStatus() == Status.AVAILABLE)
                .orElseThrow(TripNotFoundException::new);
        int totalSeats = totalSeatsOf(trip);
        long booked = seatAvailabilityRepo.countBookedSeats(tripId);
        int available = Math.max(0, totalSeats - (int) booked);
        return tripMapper.toViewModel(trip, totalSeats, available);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getOccupiedSeatCodes(UUID tripId) {
        return seatAvailabilityRepo.findOccupiedSeatCodes(tripId);
    }

    private int totalSeatsOf(Trip trip) {
        if (trip.getBus() == null || trip.getBus().getCapacity() == null) {
            return 0;
        }
        return trip.getBus().getCapacity().getSeats();
    }

    // E3 - Tao booking, giu ghe va tao thanh toan
    @Override
    @Transactional
    public UUID createBooking(CreateBookingForm form, UUID accountId) {
        Account account = findActiveById(accountId);

        Trip trip = tripRepo.findById(form.getTripId())
                .filter(t -> t.getStatus() == Status.AVAILABLE)
                .orElseThrow(TripNotFoundException::new);

        List<String> seatCodes = form.getSeatCodes();
        if (seatCodes == null || seatCodes.isEmpty()) {
            throw new SeatAlreadyBookedException("no seat selected");
        }

        // BE khong tin FE: kiem tra lai ghe co con trong khong (chong race condition)
        List<String> taken = seatAvailabilityRepo.findTakenSeatCodes(trip.getId(), seatCodes);
        if (!taken.isEmpty()) {
            throw new SeatAlreadyBookedException(String.join(", ", taken));
        }

        double unitPrice = trip.getPrice() != null ? trip.getPrice() : 0.0;
        double totalPrice = unitPrice * seatCodes.size();

        Booking booking = new Booking();
        booking.setDateBooked(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        booking.setNote(form.getNote());
        booking.setBookingType(form.getBookingType() != null ? form.getBookingType() : BookingType.ONEWAY);
        booking.setAccount(account);
        bookingRepo.save(booking);

        List<String> names = form.getPassengerNames();
        for (int i = 0; i < seatCodes.size(); i++) {
            String code = seatCodes.get(i);
            String passengerName = (names != null && i < names.size()
                    && names.get(i) != null && !names.get(i).isBlank())
                    ? names.get(i).trim() : account.getFullName();

            BookingDetail detail = new BookingDetail();
            detail.setPassengerName(passengerName);
            detail.setTicketPrice(unitPrice);
            detail.setLuggageWeightKg(0.0);
            detail.setLuggageFee(0.0);
            detail.setSubTotal(unitPrice);
            detail.setReturnTicket(false);
            detail.setBooking(booking);
            bookingDetailRepo.save(detail);

            SeatAvailability seat = new SeatAvailability();
            seat.setSeatCode(code);
            seat.setStartStationOrder(0);
            seat.setEndStationOrder(0);
            seat.setBookingDetail(detail);
            seat.setTrip(trip);
            seatAvailabilityRepo.save(seat);
        }

        Payment payment = new Payment();
        payment.setCreatePayment(LocalDateTime.now());
        payment.setPaymentMethod(form.getPaymentMethod());
        payment.setBooking(booking);
        paymentRepo.save(payment);

        return booking.getId();
    }

    // E4 - Danh sach ve cua khach hang
    @Override
    @Transactional(readOnly = true)
    public List<BookingViewModel> getMyBookings(UUID accountId) {
        List<Booking> bookings = bookingRepo.findByAccountId(accountId);
        List<BookingViewModel> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingViewModel(booking));
        }
        return result;
    }

    private BookingViewModel toBookingViewModel(Booking booking) {
        List<SeatAvailability> seats = seatAvailabilityRepo.findByBookingId(booking.getId());
        List<BookingDetail> details = bookingDetailRepo.findByBookingId(booking.getId());
        Trip trip = seats.isEmpty() ? null : seats.get(0).getTrip();

        List<String> seatCodes = new ArrayList<>();
        for (SeatAvailability seat : seats) {
            seatCodes.add(seat.getSeatCode());
        }
        List<String> passengerNames = new ArrayList<>();
        for (BookingDetail detail : details) {
            passengerNames.add(detail.getPassengerName());
        }

        boolean completed = trip != null && trip.getDepartureTime() != null
                && trip.getDepartureTime().isBefore(LocalDateTime.now());

        return new BookingViewModel(
                booking.getId(),
                booking.getDateBooked(),
                booking.getTotalPrice(),
                booking.getBookingType(),
                booking.getNote(),
                trip != null ? trip.getId() : null,
                trip != null ? trip.getDestinationFrom() : null,
                trip != null ? trip.getDestinationTo() : null,
                trip != null ? trip.getDepartureTime() : null,
                (trip != null && trip.getBus() != null) ? trip.getBus().getBusName() : null,
                trip != null ? trip.getDriverName() : null,
                seatCodes,
                passengerNames,
                completed
        );
    }
}
