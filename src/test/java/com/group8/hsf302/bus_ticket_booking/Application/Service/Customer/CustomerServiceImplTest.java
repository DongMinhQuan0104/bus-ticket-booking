package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateReviewForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.OldPasswordNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.TripMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.BookingNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.CannotCancelBookingException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.CannotReviewException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SeatAlreadyBookedException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.TripNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingDetailRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.ReviewRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock private AccountRepo accountRepo;
    @Mock private AccountMapper accountMapper;
    @Mock private PasswordHasher passwordHasher;
    @Mock private TripRepo tripRepo;
    @Mock private SeatAvailabilityRepo seatAvailabilityRepo;
    @Mock private TripMapper tripMapper;
    @Mock private BookingRepo bookingRepo;
    @Mock private BookingDetailRepo bookingDetailRepo;
    @Mock private PaymentRepo paymentRepo;
    @Mock private ReviewRepo reviewRepo;

    @InjectMocks private CustomerServiceImpl service;

    private UUID accountId;
    private UUID tripId;
    private UUID bookingId;
    private Account account;

    @BeforeEach
    void setup() {
        accountId = UUID.randomUUID();
        tripId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setFullName("Nguyen Van A");
    }

    private Trip tripWith(LocalDateTime departure) {
        Bus bus = new Bus();
        bus.setCapacity(BusCapacity.SEAT_16);
        Trip trip = new Trip();
        trip.setId(tripId);
        trip.setDestinationFrom("Ha Noi");
        trip.setDestinationTo("Hai Phong");
        trip.setDepartureTime(departure);
        trip.setStatus(Status.AVAILABLE);
        trip.setPrice(250000.0);
        trip.setBus(bus);
        return trip;
    }

    // ===== E1 - Tim kiem chuyen =====

    @Test
    void searchTrips_sameStation_throws() {
        SearchTripForm form = new SearchTripForm();
        form.setDestinationFrom("Ha Noi");
        form.setDestinationTo("ha noi"); // trung (khac hoa thuong)
        form.setDepartureDate(LocalDate.now());

        assertThrows(SameStationException.class, () -> service.searchTrips(form));
    }

    @Test
    void searchTrips_computesAvailableSeats() {
        SearchTripForm form = new SearchTripForm();
        form.setDestinationFrom("Ha Noi");
        form.setDestinationTo("Hai Phong");
        form.setDepartureDate(LocalDate.now());

        Trip trip = tripWith(LocalDateTime.now().plusDays(1));
        when(tripRepo.searchAvailable(eq("Ha Noi"), eq("Hai Phong"), any(), any()))
                .thenReturn(List.of(trip));
        when(seatAvailabilityRepo.countBookedSeats(tripId)).thenReturn(2L);
        when(tripMapper.toViewModel(eq(trip), eq(16), eq(14))).thenReturn(null);

        service.searchTrips(form);

        // 16 (SEAT_16) - 2 booked = 14 available
        verify(tripMapper).toViewModel(eq(trip), eq(16), eq(14));
    }

    // ===== E2 - Chon ghe =====

    @Test
    void getTripForBooking_notFound_throws() {
        when(tripRepo.findById(tripId)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, () -> service.getTripForBooking(tripId));
    }

    // ===== E3 - Dat ve =====

    @Test
    void createBooking_seatTaken_throws() {
        CreateBookingForm form = new CreateBookingForm();
        form.setTripId(tripId);
        form.setSeatCodes(List.of("A01", "A02"));
        form.setPaymentMethod(PaymentMethod.COD);

        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(tripRepo.findById(tripId)).thenReturn(Optional.of(tripWith(LocalDateTime.now().plusDays(1))));
        when(seatAvailabilityRepo.findTakenSeatCodes(eq(tripId), anyList())).thenReturn(List.of("A01"));

        assertThrows(SeatAlreadyBookedException.class, () -> service.createBooking(form, accountId));
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void createBooking_success_savesAll() {
        CreateBookingForm form = new CreateBookingForm();
        form.setTripId(tripId);
        form.setSeatCodes(List.of("A01", "A02"));
        form.setPassengerNames(List.of("Khach 1", "Khach 2"));
        form.setPaymentMethod(PaymentMethod.COD);

        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(tripRepo.findById(tripId)).thenReturn(Optional.of(tripWith(LocalDateTime.now().plusDays(1))));
        when(seatAvailabilityRepo.findTakenSeatCodes(eq(tripId), anyList())).thenReturn(List.of());

        service.createBooking(form, accountId);

        verify(bookingRepo).save(any(Booking.class));
        verify(bookingDetailRepo, times(2)).save(any());   // 2 ghe -> 2 chi tiet
        verify(seatAvailabilityRepo, times(2)).save(any()); // 2 ghe giu cho
        verify(paymentRepo).save(any());
    }

    // ===== E5 - Huy ve =====

    @Test
    void cancelBooking_notOwned_throws() {
        Booking booking = new Booking();
        booking.setId(bookingId);
        Account other = new Account();
        other.setId(UUID.randomUUID());
        booking.setAccount(other);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> service.cancelBooking(bookingId, accountId));
    }

    @Test
    void cancelBooking_departed_throws() {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setAccount(account);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        SeatAvailability seat = new SeatAvailability();
        seat.setTrip(tripWith(LocalDateTime.now().minusDays(1))); // da khoi hanh
        when(seatAvailabilityRepo.findByBookingId(bookingId)).thenReturn(List.of(seat));

        assertThrows(CannotCancelBookingException.class, () -> service.cancelBooking(bookingId, accountId));
        verify(bookingRepo, never()).delete(any());
    }

    @Test
    void cancelBooking_success_deletes() {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setAccount(account);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        SeatAvailability seat = new SeatAvailability();
        seat.setTrip(tripWith(LocalDateTime.now().plusDays(1))); // chua khoi hanh
        when(seatAvailabilityRepo.findByBookingId(bookingId)).thenReturn(List.of(seat));
        when(paymentRepo.findByBookingId(bookingId)).thenReturn(List.of());
        when(bookingDetailRepo.findByBookingId(bookingId)).thenReturn(List.of());

        service.cancelBooking(bookingId, accountId);

        verify(seatAvailabilityRepo).delete(seat);
        verify(bookingRepo).delete(booking);
    }

    // ===== E6 - Danh gia =====

    @Test
    void reviewBooking_notCompleted_throws() {
        CreateReviewForm form = new CreateReviewForm();
        form.setRating(5);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setAccount(account);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        SeatAvailability seat = new SeatAvailability();
        seat.setTrip(tripWith(LocalDateTime.now().plusDays(1))); // chua khoi hanh -> chua the danh gia
        when(seatAvailabilityRepo.findByBookingId(bookingId)).thenReturn(List.of(seat));

        assertThrows(CannotReviewException.class, () -> service.reviewBooking(bookingId, accountId, form));
        verify(reviewRepo, never()).save(any());
    }

    @Test
    void reviewBooking_alreadyReviewed_throws() {
        CreateReviewForm form = new CreateReviewForm();
        form.setRating(4);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setAccount(account);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        SeatAvailability seat = new SeatAvailability();
        seat.setTrip(tripWith(LocalDateTime.now().minusDays(1))); // da hoan thanh
        when(seatAvailabilityRepo.findByBookingId(bookingId)).thenReturn(List.of(seat));
        when(reviewRepo.existsByBookingId(bookingId)).thenReturn(true);

        assertThrows(CannotReviewException.class, () -> service.reviewBooking(bookingId, accountId, form));
        verify(reviewRepo, never()).save(any());
    }

    // ===== Ho so ca nhan (getAccount/update/changePassword/deleted) =====

    @Test
    void deleted_softDelete_setsNotAvailable() {
        account.setStatus(Status.AVAILABLE);
        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));

        boolean result = service.deleted(accountId);

        assertEquals(true, result);
        assertEquals(Status.NOT_AVAILABLE, account.getStatus());
        verify(accountRepo).save(account);
    }

    @Test
    void update_savesAndReturnsViewModel() {
        UpdateAccountForm form = new UpdateAccountForm();
        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.updateEntityFromForm(form, account)).thenReturn(account);

        service.update(form, accountId);

        verify(accountRepo).save(account);
        verify(accountMapper).toViewModel(account);
    }

    @Test
    void changePassword_wrongOldPassword_throws() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setOldPassword("sai");
        form.setNewPassword("newpass");
        form.setConfirmNewPassword("newpass");
        account.setPassword("hashed");
        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(passwordHasher.verify("sai", "hashed")).thenReturn(false);

        assertThrows(OldPasswordNotMatchException.class, () -> service.changePassword(form, accountId));
        verify(accountRepo, never()).save(any());
    }

    @Test
    void changePassword_confirmMismatch_throws() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setOldPassword("old");
        form.setNewPassword("aaa");
        form.setConfirmNewPassword("bbb");
        account.setPassword("hashed");
        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(passwordHasher.verify("old", "hashed")).thenReturn(true);

        assertThrows(PasswordConfirmNotMatchException.class, () -> service.changePassword(form, accountId));
        verify(accountRepo, never()).save(any());
    }

    @Test
    void changePassword_success_savesHashed() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setOldPassword("old");
        form.setNewPassword("newpass");
        form.setConfirmNewPassword("newpass");
        account.setPassword("hashed");
        when(accountRepo.findActiveById(accountId)).thenReturn(Optional.of(account));
        when(passwordHasher.verify("old", "hashed")).thenReturn(true);
        when(passwordHasher.hash("newpass")).thenReturn("newhashed");

        boolean result = service.changePassword(form, accountId);

        assertEquals(true, result);
        assertEquals("newhashed", account.getPassword());
        verify(accountRepo).save(account);
    }

    @Test
    void reviewBooking_success_saves() {
        CreateReviewForm form = new CreateReviewForm();
        form.setRating(5);
        form.setComment("Tuyet voi");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setAccount(account);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));

        SeatAvailability seat = new SeatAvailability();
        seat.setTrip(tripWith(LocalDateTime.now().minusDays(1))); // da hoan thanh
        when(seatAvailabilityRepo.findByBookingId(bookingId)).thenReturn(List.of(seat));
        when(reviewRepo.existsByBookingId(bookingId)).thenReturn(false);

        service.reviewBooking(bookingId, accountId, form);

        verify(reviewRepo).save(any());
    }
}
