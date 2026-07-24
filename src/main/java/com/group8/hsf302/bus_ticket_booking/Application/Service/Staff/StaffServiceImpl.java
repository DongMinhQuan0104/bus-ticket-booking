package com.group8.hsf302.bus_ticket_booking.Application.Service.Staff;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.StaffBusinessException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    /*
     * Tài liệu nghiệp vụ chưa quy định chính xác số phút giữ ghế.
     * Project tạm sử dụng 10 phút.
     *
     * Sau này có thể đưa giá trị này vào application.properties.
     */
    private static final long HOLD_MINUTES = 10L;

    private static final DateTimeFormatter CODE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final AccountRepo accountRepo;
    private final TripRepo tripRepo;
    private final RouteStationRepo routeStationRepo;
    private final BookingRepo bookingRepo;
    private final BookingDetailRepo bookingDetailRepo;
    private final SeatAvailabilityRepo seatAvailabilityRepo;
    private final PaymentRepo paymentRepo;
    private final TicketRepo ticketRepo;
    private final SupportRequestRepo supportRequestRepo;
    private final StaffActionLogRepo staffActionLogRepo;

    public StaffServiceImpl(
            AccountRepo accountRepo,
            TripRepo tripRepo,
            RouteStationRepo routeStationRepo,
            BookingRepo bookingRepo,
            BookingDetailRepo bookingDetailRepo,
            SeatAvailabilityRepo seatAvailabilityRepo,
            PaymentRepo paymentRepo,
            TicketRepo ticketRepo,
            SupportRequestRepo supportRequestRepo,
            StaffActionLogRepo staffActionLogRepo
    ) {
        this.accountRepo = accountRepo;
        this.tripRepo = tripRepo;
        this.routeStationRepo = routeStationRepo;
        this.bookingRepo = bookingRepo;
        this.bookingDetailRepo = bookingDetailRepo;
        this.seatAvailabilityRepo = seatAvailabilityRepo;
        this.paymentRepo = paymentRepo;
        this.ticketRepo = ticketRepo;
        this.supportRequestRepo = supportRequestRepo;
        this.staffActionLogRepo = staffActionLogRepo;
    }

    // =========================================================
    // C1. TÌM CHUYẾN VÀ XEM GHẾ
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StaffTripViewModel> searchTrips(
            StaffTripSearchForm form
    ) {
        validateTripSearchForm(form);

        LocalDateTime startOfDay =
                form.departureDate().atStartOfDay();

        LocalDateTime endOfDay =
                form.departureDate().atTime(LocalTime.MAX);

        List<Trip> trips = tripRepo.searchAvailableTrips(
                form.destinationFrom().trim(),
                form.destinationTo().trim(),
                startOfDay,
                endOfDay
        );

        LocalDateTime now = LocalDateTime.now();

        return trips.stream()
                .filter(Objects::nonNull)
                .filter(trip ->
                        trip.getStatus() == Status.AVAILABLE
                )
                .filter(trip ->
                        trip.getDepartureTime() != null
                                && trip.getDepartureTime().isAfter(now)
                )
                .map(this::toTripViewModel)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public StaffTripViewModel getTrip(
            UUID tripId
    ) {
        Trip trip = requireSellableTrip(tripId);

        return toTripViewModel(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffRouteStationViewModel> getRouteStations(
            UUID tripId
    ) {
        Trip trip = requireSellableTrip(tripId);

        List<RouteStation> routeStations =
                routeStationRepo.findByRouteId(
                        trip.getRoute().getId()
                );

        if (routeStations == null || routeStations.isEmpty()) {
            throw new StaffBusinessException(
                    "Tuyến của chuyến xe chưa có điểm đón và điểm trả."
            );
        }

        List<StaffRouteStationViewModel> result =
                routeStations.stream()
                        .filter(Objects::nonNull)
                        .filter(routeStation ->
                                routeStation.getStation() != null
                        )
                        .filter(routeStation ->
                                routeStation.getStationOrder() != null
                        )
                        .sorted(
                                Comparator.comparing(
                                        RouteStation::getStationOrder
                                )
                        )
                        .map(routeStation ->
                                new StaffRouteStationViewModel(
                                        routeStation.getStationOrder(),
                                        routeStation.getStation().getName(),
                                        routeStation.getStation().getAddress(),
                                        routeStation.getPriceFromStart()
                                )
                        )
                        .toList();

        if (result.size() < 2) {
            throw new StaffBusinessException(
                    "Tuyến xe phải có ít nhất một điểm đón và một điểm trả."
            );
        }

        return result;
    }




    @Override
    @Transactional(readOnly = true)
    public List<StaffSeatViewModel> getSeats(
            UUID tripId,
            Integer pickupOrder,
            Integer dropoffOrder
    ) {
        Trip trip = requireSellableTrip(tripId);

        validateStationOrders(
                trip,
                pickupOrder,
                dropoffOrder
        );

        List<SeatAvailability> segments =
                seatAvailabilityRepo.findOverlappingSegments(
                        tripId,
                        pickupOrder,
                        dropoffOrder
                );

        return aggregateSeatStatuses(segments);
    }

    // =========================================================
    // C1. TẠO BOOKING TẠI QUẦY
    // =========================================================

    @Override
    @Transactional
    public StaffBookingViewModel createCounterBooking(
            StaffCreateBookingForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);
        validateCreateBookingForm(form);

        Trip trip = requireSellableTrip(form.tripId());

        RouteStation pickupStation = validateStationOrders(
                trip,
                form.pickupStationOrder(),
                form.dropoffStationOrder()
        ).pickupStation();

        RouteStation dropoffStation =
                requireRouteStation(
                        trip,
                        form.dropoffStationOrder()
                );

        double ticketPrice = calculateTicketPrice(
                pickupStation,
                dropoffStation
        );

        List<String> requestedSeatCodes =
                normalizeAndValidateSeatCodes(form.passengers());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt =
                now.plusMinutes(HOLD_MINUTES);

        Booking booking = new Booking();
        booking.setBookingCode(generateBookingCode());
        booking.setDateBooked(now);
        booking.setTotalPrice(0.0);
        booking.setNote(normalizeNullable(form.note()));
        booking.setBookingType(form.bookingType());
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setContactName(form.contactName().trim());
        booking.setContactPhone(form.contactPhone().trim());
        booking.setContactEmail(
                normalizeNullable(form.contactEmail())
        );
        booking.setExpiresAt(expiresAt);
        booking.setAccount(null);
        booking.setCreatedBy(staff);
        booking.setTrip(trip);

        Map<String, BookingDetail> detailBySeat =
                new LinkedHashMap<>();

        double totalPrice = 0.0;

        for (StaffPassengerForm passengerForm
                : form.passengers()) {

            String seatCode =
                    normalizeSeatCode(passengerForm.seatCode());

            double luggageWeight =
                    normalizeNonNegative(
                            passengerForm.luggageWeightKg()
                    );

            /*
             * Tài liệu hiện chưa quy định chính sách phí hành lý.
             * Vì vậy tạm thời luggageFee = 0.
             */
            double luggageFee = 0.0;
            double subTotal = ticketPrice + luggageFee;

            BookingDetail detail = new BookingDetail();
            detail.setPassengerName(
                    passengerForm.passengerName().trim()
            );
            detail.setSeatCode(seatCode);
            detail.setTicketPrice(ticketPrice);
            detail.setLuggageWeightKg(luggageWeight);
            detail.setLuggageFee(luggageFee);
            detail.setSubTotal(subTotal);
            detail.setReturnTicket(
                    form.bookingType() == BookingType.ROUNDTRIP
            );
            detail.setPickupStationOrder(
                    form.pickupStationOrder()
            );
            detail.setDropoffStationOrder(
                    form.dropoffStationOrder()
            );

            booking.addDetail(detail);
            detailBySeat.put(seatCode, detail);

            totalPrice += subTotal;
        }

        booking.setTotalPrice(totalPrice);

        /*
         * Lưu Booking và BookingDetail trước để các Detail có ID.
         * Booking.details phải có cascade = CascadeType.ALL.
         */
        Booking savedBooking = bookingRepo.save(booking);

        /*
         * Khóa các record ghế liên quan bằng PESSIMISTIC_WRITE.
         * Hai Staff/Customer không thể đồng thời đặt cùng một ghế.
         */
        List<SeatAvailability> lockedSegments =
                seatAvailabilityRepo.lockOverlappingSegments(
                        trip.getId(),
                        requestedSeatCodes,
                        form.pickupStationOrder(),
                        form.dropoffStationOrder()
                );

        validateRequestedSeatSegments(
                requestedSeatCodes,
                lockedSegments
        );

        releaseExpiredSegmentsInMemory(
                lockedSegments,
                now
        );

        validateSeatsAreAvailable(
                requestedSeatCodes,
                lockedSegments
        );

        for (SeatAvailability segment : lockedSegments) {
            BookingDetail detail =
                    detailBySeat.get(segment.getSeatCode());

            if (detail == null) {
                throw new StaffBusinessException(
                        "Booking detail was not found for seat "
                                + segment.getSeatCode()
                );
            }

            segment.hold(detail, expiresAt);
        }

        seatAvailabilityRepo.saveAll(lockedSegments);

        saveActionLog(
                staff,
                savedBooking,
                null,
                StaffActionType.COUNTER_BOOKING_CREATED,
                "Created counter booking "
                        + savedBooking.getBookingCode()
        );

        return toBookingViewModel(savedBooking);
    }

    // =========================================================
    // C1. XÁC NHẬN THANH TOÁN VÀ SINH VÉ
    // =========================================================

    @Override
    @Transactional
    public StaffBookingViewModel confirmPayment(
            UUID bookingId,
            StaffConfirmPaymentForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);
        Booking booking = requireBooking(bookingId);

        validateCounterPaymentForm(form);

        Optional<Payment> latestPayment =
                paymentRepo.findLatestByBookingId(
                        bookingId
                );

        /*
         * Idempotency:
         * Request thanh toán bị gửi lại sẽ trả về Booking cũ,
         * không tạo thêm Payment hoặc Ticket.
         */
        if (booking.getStatus()
                == BookingStatus.CONFIRMED
                && latestPayment.isPresent()
                && latestPayment.get().getStatus()
                == PaymentStatus.PAID) {
            return toBookingViewModel(booking);
        }

        if (booking.getStatus()
                != BookingStatus.PENDING_PAYMENT) {
            throw new StaffBusinessException(
                    "Only pending bookings can be paid"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        if (booking.getExpiresAt() == null
                || !booking.getExpiresAt().isAfter(now)) {
            expireBookingAndReleaseSeats(booking);

            throw new StaffBusinessException(
                    "Booking has expired"
            );
        }

        if (latestPayment.isPresent()
                && latestPayment.get().getStatus()
                == PaymentStatus.PAID) {
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setExpiresAt(null);

            Booking savedBooking =
                    bookingRepo.save(booking);

            issueTickets(
                    getBookingDetails(savedBooking)
            );

            return toBookingViewModel(savedBooking);
        }

        List<BookingDetail> details =
                getBookingDetails(booking);

        if (details.isEmpty()) {
            throw new StaffBusinessException(
                    "Booking does not contain any ticket"
            );
        }

        List<UUID> detailIds = details.stream()
                .map(BookingDetail::getId)
                .filter(Objects::nonNull)
                .toList();

        List<SeatAvailability> heldSegments =
                seatAvailabilityRepo
                        .findByBookingDetailIds(detailIds);

        if (heldSegments.isEmpty()) {
            throw new StaffBusinessException(
                    "No held seats were found for this booking"
            );
        }

        boolean containsInvalidSeat =
                heldSegments.stream().anyMatch(segment ->
                        segment.getStatus()
                                != SeatStatus.HELD
                                || segment.isHoldExpired(now)
                );

        if (containsInvalidSeat) {
            expireBookingAndReleaseSeats(booking);

            throw new StaffBusinessException(
                    "One or more held seats have expired"
            );
        }

        Payment payment = new Payment();
        payment.setCreatePayment(now);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(
                form.paymentMethod()
        );
        payment.setStatus(PaymentStatus.PENDING);
        payment.setReferenceCode(
                normalizeNullable(form.referenceCode())
        );
        payment.setBooking(booking);
        payment.markAsPaid(staff, now);

        paymentRepo.save(payment);

        heldSegments.forEach(
                SeatAvailability::markAsBooked
        );

        seatAvailabilityRepo.saveAll(
                heldSegments
        );

        booking.setStatus(
                BookingStatus.CONFIRMED
        );
        booking.setExpiresAt(null);

        Booking confirmedBooking =
                bookingRepo.save(booking);

        issueTickets(details);

        saveActionLog(
                staff,
                confirmedBooking,
                null,
                StaffActionType.PAYMENT_CONFIRMED,
                "Confirmed "
                        + form.paymentMethod()
                        + " payment for booking "
                        + confirmedBooking.getBookingCode()
        );

        return toBookingViewModel(
                confirmedBooking
        );
    }

    // =========================================================
    // C2. TRA CỨU VÀ XEM BOOKING
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StaffBookingViewModel> searchBookings(
            String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            throw new StaffBusinessException(
                    "Search keyword can not be blank"
            );
        }

        return bookingRepo.search(keyword.trim())
                .stream()
                .map(this::toBookingViewModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StaffBookingViewModel getBooking(
            UUID bookingId
    ) {
        return toBookingViewModel(
                requireBooking(bookingId)
        );
    }

    // =========================================================
    // C2. ĐỔI GHẾ
    // =========================================================

    @Override
    @Transactional
    public StaffBookingViewModel changeSeat(
            UUID bookingId,
            StaffChangeSeatForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);
        Booking booking = requireBooking(bookingId);

        requireModifiableBooking(booking);

        BookingDetail detail =
                bookingDetailRepo.findById(
                                form.bookingDetailId()
                        )
                        .orElseThrow(() ->
                                new StaffBusinessException(
                                        "Booking detail was not found"
                                )
                        );

        if (detail.getBooking() == null
                || !bookingId.equals(
                detail.getBooking().getId()
        )) {
            throw new StaffBusinessException(
                    "Booking detail does not belong to booking"
            );
        }

        Ticket ticket = ticketRepo
                .findByBookingDetailId(detail.getId())
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Ticket was not found"
                        )
                );

        if (ticket.getStatus()
                == TicketStatus.CHECKED_IN) {
            throw new StaffBusinessException(
                    "Checked-in ticket can not change seat"
            );
        }

        if (ticket.getStatus()
                != TicketStatus.VALID) {
            throw new StaffBusinessException(
                    "Only valid ticket can change seat"
            );
        }

        String oldSeatCode = detail.getSeatCode();
        String newSeatCode =
                normalizeSeatCode(form.newSeatCode());

        if (oldSeatCode.equalsIgnoreCase(newSeatCode)) {
            throw new StaffBusinessException(
                    "New seat must be different from old seat"
            );
        }

        Trip trip = booking.getTrip();

        List<SeatAvailability> newSeatSegments =
                seatAvailabilityRepo.lockOverlappingSegments(
                        trip.getId(),
                        List.of(newSeatCode),
                        detail.getPickupStationOrder(),
                        detail.getDropoffStationOrder()
                );

        validateRequestedSeatSegments(
                List.of(newSeatCode),
                newSeatSegments
        );

        LocalDateTime now = LocalDateTime.now();

        releaseExpiredSegmentsInMemory(
                newSeatSegments,
                now
        );

        validateSeatsAreAvailable(
                List.of(newSeatCode),
                newSeatSegments
        );

        List<SeatAvailability> oldSeatSegments =
                seatAvailabilityRepo.findByBookingDetailIds(
                        List.of(detail.getId())
                );

        oldSeatSegments.forEach(
                SeatAvailability::release
        );

        newSeatSegments.forEach(segment -> {
            segment.setBookingDetail(detail);
            segment.setStatus(SeatStatus.BOOKED);
            segment.setHoldExpiredAt(null);
        });

        detail.setSeatCode(newSeatCode);

        bookingDetailRepo.save(detail);

        List<SeatAvailability> updatedSegments =
                new ArrayList<>();

        updatedSegments.addAll(oldSeatSegments);
        updatedSegments.addAll(newSeatSegments);

        seatAvailabilityRepo.saveAll(updatedSegments);

        saveActionLog(
                staff,
                booking,
                ticket,
                StaffActionType.SEAT_CHANGED,
                "Changed seat from "
                        + oldSeatCode
                        + " to "
                        + newSeatCode
                        + " for booking "
                        + booking.getBookingCode()
        );

        return toBookingViewModel(booking);
    }

    // =========================================================
    // C2. HỦY BOOKING
    // =========================================================

    @Override
    @Transactional
    public StaffBookingViewModel cancelBooking(
            UUID bookingId,
            StaffCancelBookingForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);
        Booking booking = requireBooking(bookingId);

        if (form == null
                || form.reason() == null
                || form.reason().isBlank()) {
            throw new StaffBusinessException(
                    "Cancellation reason can not be blank"
            );
        }

        BookingStatus originalStatus =
                booking.getStatus();

        if (originalStatus
                != BookingStatus.CONFIRMED
                && originalStatus
                != BookingStatus.PENDING_PAYMENT) {
            throw new StaffBusinessException(
                    "Booking can not be cancelled in current status"
            );
        }

        List<Ticket> tickets =
                ticketRepo.findByBookingId(bookingId);

        boolean hasCheckedInTicket =
                tickets.stream().anyMatch(ticket ->
                        ticket.getStatus()
                                == TicketStatus.CHECKED_IN
                );

        if (hasCheckedInTicket) {
            throw new StaffBusinessException(
                    "Booking contains checked-in ticket"
            );
        }

        if (form.requestRefund()) {
            if (originalStatus
                    != BookingStatus.CONFIRMED) {
                throw new StaffBusinessException(
                        "Unpaid booking can not request refund"
                );
            }

            Payment payment =
                    paymentRepo
                            .findLatestByBookingId(
                                    bookingId
                            )
                            .orElseThrow(() ->
                                    new StaffBusinessException(
                                            "Paid payment was not found"
                                    )
                            );

            if (payment.getStatus()
                    != PaymentStatus.PAID) {
                throw new StaffBusinessException(
                        "Only paid booking can request refund"
                );
            }
        }

        releaseBookingSeats(booking);

        tickets.forEach(ticket -> {
            if (ticket.getStatus()
                    == TicketStatus.VALID) {
                ticket.setStatus(
                        TicketStatus.CANCELLED
                );
            }
        });

        if (!tickets.isEmpty()) {
            ticketRepo.saveAll(tickets);
        }

        booking.setStatus(
                form.requestRefund()
                        ? BookingStatus.REFUND_PENDING
                        : BookingStatus.CANCELLED
        );

        booking.setExpiresAt(null);

        booking.setNote(
                appendNote(
                        booking.getNote(),
                        "Cancellation reason: "
                                + form.reason().trim()
                )
        );

        Booking cancelledBooking =
                bookingRepo.save(booking);

        saveActionLog(
                staff,
                cancelledBooking,
                null,
                StaffActionType.BOOKING_CANCELLED,
                "Cancelled booking "
                        + cancelledBooking.getBookingCode()
                        + ". Reason: "
                        + form.reason().trim()
        );

        return toBookingViewModel(
                cancelledBooking
        );
    }

    // =========================================================
    // C2. XỬ LÝ HOÀN TIỀN
    // =========================================================

    @Override
    @Transactional
    public StaffBookingViewModel processRefund(
            UUID bookingId,
            StaffRefundForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);
        Booking booking = requireBooking(bookingId);

        if (booking.getStatus()
                != BookingStatus.REFUND_PENDING
                && booking.getStatus()
                != BookingStatus.CANCELLED) {
            throw new StaffBusinessException(
                    "Booking is not waiting for refund"
            );
        }

        if (form.escalateToAdmin()) {
            booking.setStatus(
                    BookingStatus.REFUND_PENDING
            );

            booking.setNote(
                    appendNote(
                            booking.getNote(),
                            "Refund escalated to Admin: "
                                    + form.reason().trim()
                    )
            );

            Booking pendingBooking =
                    bookingRepo.save(booking);

            saveActionLog(
                    staff,
                    pendingBooking,
                    null,
                    StaffActionType.SUPPORT_REQUEST_ESCALATED,
                    "Escalated refund for booking "
                            + booking.getBookingCode()
            );

            return toBookingViewModel(pendingBooking);
        }

        Payment payment =
                paymentRepo.findLatestByBookingId(bookingId)
                        .orElseThrow(() ->
                                new StaffBusinessException(
                                        "Payment was not found"
                                )
                        );

        if (payment.getStatus()
                != PaymentStatus.PAID) {
            throw new StaffBusinessException(
                    "Only paid payment can be refunded"
            );
        }

        payment.markAsRefunded();
        paymentRepo.save(payment);

        List<Ticket> tickets =
                ticketRepo.findByBookingId(bookingId);

        tickets.forEach(ticket -> {
            if (ticket.getStatus()
                    != TicketStatus.CHECKED_IN) {
                ticket.setStatus(
                        TicketStatus.REFUNDED
                );
            }
        });

        ticketRepo.saveAll(tickets);

        releaseBookingSeats(booking);

        booking.setStatus(BookingStatus.REFUNDED);
        booking.setNote(
                appendNote(
                        booking.getNote(),
                        "Refund reason: "
                                + form.reason().trim()
                )
        );

        Booking refundedBooking =
                bookingRepo.save(booking);

        saveActionLog(
                staff,
                refundedBooking,
                null,
                StaffActionType.REFUND_PROCESSED,
                "Processed refund for booking "
                        + refundedBooking.getBookingCode()
        );

        return toBookingViewModel(refundedBooking);
    }

    // =========================================================
    // C3. CHECK-IN
    // =========================================================

    @Override
    @Transactional
    public StaffCheckInViewModel checkIn(
            StaffCheckInForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);

        Ticket ticket = ticketRepo
                .findByTicketCode(form.ticketCode().trim())
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Ticket was not found"
                        )
                );

        if (ticket.getStatus()
                == TicketStatus.CHECKED_IN) {
            throw new StaffBusinessException(
                    "Ticket has already been checked in"
            );
        }

        if (ticket.getStatus()
                == TicketStatus.CANCELLED) {
            throw new StaffBusinessException(
                    "Cancelled ticket can not be checked in"
            );
        }

        if (ticket.getStatus()
                == TicketStatus.REFUNDED) {
            throw new StaffBusinessException(
                    "Refunded ticket can not be checked in"
            );
        }

        if (ticket.getStatus()
                != TicketStatus.VALID) {
            throw new StaffBusinessException(
                    "Ticket is not valid"
            );
        }

        BookingDetail detail =
                ticket.getBookingDetail();

        if (detail == null
                || detail.getBooking() == null
                || detail.getBooking().getTrip() == null) {
            throw new StaffBusinessException(
                    "Ticket booking data is invalid"
            );
        }

        Booking booking = detail.getBooking();
        Trip trip = booking.getTrip();

        if (!trip.getId().equals(form.tripId())) {
            throw new StaffBusinessException(
                    "Ticket belongs to another trip"
            );
        }

        if (booking.getStatus()
                != BookingStatus.CONFIRMED) {
            throw new StaffBusinessException(
                    "Booking is not confirmed"
            );
        }

        if (trip.getDepartureTime() == null) {
            throw new StaffBusinessException(
                    "Trip departure time is invalid"
            );
        }

        if (!trip.getDepartureTime()
                .toLocalDate()
                .equals(LocalDate.now())) {
            throw new StaffBusinessException(
                    "Ticket is not valid for today"
            );
        }

        if (trip.getStatus()
                == Status.NOT_AVAILABLE) {
            throw new StaffBusinessException(
                    "Trip is not available"
            );
        }

        LocalDateTime checkedInAt =
                LocalDateTime.now();

        ticket.setStatus(TicketStatus.CHECKED_IN);
        ticket.setCheckedInAt(checkedInAt);
        ticket.setCheckedInBy(staff);

        Ticket checkedInTicket =
                ticketRepo.save(ticket);

        saveActionLog(
                staff,
                booking,
                checkedInTicket,
                StaffActionType.TICKET_CHECKED_IN,
                "Checked in ticket "
                        + checkedInTicket.getTicketCode()
        );

        return new StaffCheckInViewModel(
                checkedInTicket.getTicketCode(),
                checkedInTicket.getStatus(),
                checkedInTicket.getCheckedInAt(),
                detail.getPassengerName(),
                detail.getSeatCode(),
                "Passenger checked in successfully"
        );
    }

    // =========================================================
    // C4. HỖ TRỢ KHÁCH HÀNG
    // =========================================================

    @Override
    @Transactional
    public StaffSupportRequestViewModel createSupportRequest(
            StaffSupportRequestForm form,
            UUID staffId
    ) {
        Account staff = requireActiveStaff(staffId);

        Booking booking = null;

        if (form.bookingId() != null) {
            booking = requireBooking(form.bookingId());
        }

        SupportRequest request = new SupportRequest();
        request.setSubject(form.subject().trim());
        request.setDescription(
                form.description().trim()
        );
        request.setCreatedAt(LocalDateTime.now());
        request.setBooking(booking);
        request.setCreatedBy(staff);

        if (form.escalateToAdmin()) {
            request.setStatus(
                    SupportRequestStatus.ESCALATED
            );
        } else {
            request.setStatus(
                    SupportRequestStatus.OPEN
            );
        }

        SupportRequest savedRequest =
                supportRequestRepo.save(request);

        saveActionLog(
                staff,
                booking,
                null,
                form.escalateToAdmin()
                        ? StaffActionType
                          .SUPPORT_REQUEST_ESCALATED
                        : StaffActionType
                          .SUPPORT_REQUEST_CREATED,
                "Created support request: "
                        + savedRequest.getSubject()
        );

        return new StaffSupportRequestViewModel(
                savedRequest.getId(),
                savedRequest.getSubject(),
                savedRequest.getDescription(),
                savedRequest.getStatus(),
                savedRequest.getCreatedAt()
        );
    }

    // =========================================================
    // GIẢI PHÓNG GHẾ HẾT HẠN
    // =========================================================

    @Override
    @Transactional
    public int releaseExpiredHolds() {
        LocalDateTime now = LocalDateTime.now();

        List<SeatAvailability> expiredSeats =
                seatAvailabilityRepo
                        .findExpiredHeldSeats(now);

        if (expiredSeats.isEmpty()) {
            return 0;
        }

        Set<Booking> expiredBookings =
                expiredSeats.stream()
                        .map(
                                SeatAvailability
                                        ::getBookingDetail
                        )
                        .filter(Objects::nonNull)
                        .map(BookingDetail::getBooking)
                        .filter(Objects::nonNull)
                        .collect(
                                Collectors.toCollection(
                                        LinkedHashSet::new
                                )
                        );

        expiredSeats.forEach(
                SeatAvailability::release
        );

        seatAvailabilityRepo.saveAll(expiredSeats);

        for (Booking booking : expiredBookings) {
            if (booking.getStatus()
                    == BookingStatus.PENDING_PAYMENT) {
                booking.setStatus(
                        BookingStatus.EXPIRED
                );
                bookingRepo.save(booking);
            }
        }

        return expiredSeats.size();
    }

    // =========================================================
    // PRIVATE: VALIDATION
    // =========================================================

    private void validateTripSearchForm(
            StaffTripSearchForm form
    ) {
        if (form == null) {
            throw new StaffBusinessException(
                    "Trip search form can not be null"
            );
        }

        if (form.destinationFrom() == null
                || form.destinationFrom().isBlank()) {
            throw new StaffBusinessException(
                    "Destination from can not be blank"
            );
        }

        if (form.destinationTo() == null
                || form.destinationTo().isBlank()) {
            throw new StaffBusinessException(
                    "Destination to can not be blank"
            );
        }

        if (form.destinationFrom().trim()
                .equalsIgnoreCase(
                        form.destinationTo().trim()
                )) {
            throw new StaffBusinessException(
                    "Destination from and to must be different"
            );
        }

        if (form.departureDate() == null) {
            throw new StaffBusinessException(
                    "Departure date can not be null"
            );
        }

        if (form.departureDate()
                .isBefore(LocalDate.now())) {
            throw new StaffBusinessException(
                    "Departure date can not be in the past"
            );
        }
    }

    private void validateCreateBookingForm(
            StaffCreateBookingForm form
    ) {
        if (form == null) {
            throw new StaffBusinessException(
                    "Booking form can not be null"
            );
        }

        if (form.tripId() == null) {
            throw new StaffBusinessException(
                    "Trip can not be null"
            );
        }

        if (form.pickupStationOrder() == null
                || form.dropoffStationOrder() == null) {
            throw new StaffBusinessException(
                    "Pickup and dropoff stations are required"
            );
        }

        if (form.pickupStationOrder()
                >= form.dropoffStationOrder()) {
            throw new StaffBusinessException(
                    "Dropoff station must be after pickup station"
            );
        }

        if (form.contactName() == null
                || form.contactName().isBlank()) {
            throw new StaffBusinessException(
                    "Contact name can not be blank"
            );
        }

        if (form.contactPhone() == null
                || form.contactPhone().isBlank()) {
            throw new StaffBusinessException(
                    "Contact phone can not be blank"
            );
        }

        if (form.bookingType() == null) {
            throw new StaffBusinessException(
                    "Booking type can not be null"
            );
        }

        if (form.passengers() == null
                || form.passengers().isEmpty()) {
            throw new StaffBusinessException(
                    "At least one passenger is required"
            );
        }

        for (StaffPassengerForm passenger
                : form.passengers()) {

            if (passenger == null) {
                throw new StaffBusinessException(
                        "Passenger information can not be null"
                );
            }

            if (passenger.passengerName() == null
                    || passenger.passengerName().isBlank()) {
                throw new StaffBusinessException(
                        "Passenger name can not be blank"
                );
            }

            if (passenger.seatCode() == null
                    || passenger.seatCode().isBlank()) {
                throw new StaffBusinessException(
                        "Passenger seat can not be blank"
                );
            }

            if (passenger.luggageWeightKg() != null
                    && passenger.luggageWeightKg() < 0) {
                throw new StaffBusinessException(
                        "Luggage weight can not be negative"
                );
            }
        }
    }

    private void validateCounterPaymentForm(
            StaffConfirmPaymentForm form
    ) {
        if (form == null
                || form.paymentMethod() == null) {
            throw new StaffBusinessException(
                    "Payment method is required"
            );
        }

        if (form.paymentMethod()
                != PaymentMethod.CASH
                && form.paymentMethod()
                != PaymentMethod.BANK_TRANSFER) {
            throw new StaffBusinessException(
                    "Staff only accepts cash or bank transfer"
            );
        }

        if (form.paymentMethod()
                == PaymentMethod.BANK_TRANSFER
                && (form.referenceCode() == null
                || form.referenceCode().isBlank())) {
            throw new StaffBusinessException(
                    "Reference code is required for bank transfer"
            );
        }
    }

    private Account requireActiveStaff(UUID staffId) {
        if (staffId == null) {
            throw new StaffBusinessException(
                    "Staff ID can not be null"
            );
        }

        Account staff = accountRepo
                .findActiveById(staffId)
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Active staff account was not found"
                        )
                );

        if (staff.getRole() != Role.STAFF) {
            throw new StaffBusinessException(
                    "Account does not have STAFF role"
            );
        }

        if (staff.getStatus() != Status.AVAILABLE) {
            throw new StaffBusinessException(
                    "Staff account is not available"
            );
        }

        return staff;
    }

    private Trip requireSellableTrip(UUID tripId) {
        if (tripId == null) {
            throw new StaffBusinessException(
                    "Trip ID can not be null"
            );
        }

        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Trip was not found"
                        )
                );

        if (trip.getStatus() != Status.AVAILABLE) {
            throw new StaffBusinessException(
                    "Trip is not available for sale"
            );
        }

        if (trip.getDepartureTime() == null
                || !trip.getDepartureTime()
                .isAfter(LocalDateTime.now())) {
            throw new StaffBusinessException(
                    "Trip has already departed"
            );
        }

        if (trip.getRoute() == null) {
            throw new StaffBusinessException(
                    "Trip route data is missing"
            );
        }

        return trip;
    }

    private Booking requireBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new StaffBusinessException(
                    "Booking ID can not be null"
            );
        }

        return bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Booking was not found"
                        )
                );
    }

    private void requireModifiableBooking(
            Booking booking
    ) {
        if (booking.getStatus()
                != BookingStatus.CONFIRMED) {
            throw new StaffBusinessException(
                    "Only confirmed booking can be modified"
            );
        }

        if (booking.getTrip() == null
                || booking.getTrip()
                .getDepartureTime() == null
                || !booking.getTrip()
                .getDepartureTime()
                .isAfter(LocalDateTime.now())) {
            throw new StaffBusinessException(
                    "Booking trip has already departed"
            );
        }
    }

    private StationValidationResult validateStationOrders(
            Trip trip,
            Integer pickupOrder,
            Integer dropoffOrder
    ) {
        if (pickupOrder == null
                || dropoffOrder == null) {
            throw new StaffBusinessException(
                    "Station orders can not be null"
            );
        }

        if (pickupOrder >= dropoffOrder) {
            throw new StaffBusinessException(
                    "Dropoff station must be after pickup station"
            );
        }

        RouteStation pickup =
                requireRouteStation(trip, pickupOrder);

        RouteStation dropoff =
                requireRouteStation(trip, dropoffOrder);

        return new StationValidationResult(
                pickup,
                dropoff
        );
    }

    private RouteStation requireRouteStation(
            Trip trip,
            Integer stationOrder
    ) {
        return routeStationRepo
                .findByRouteIdAndStationOrder(
                        trip.getRoute().getId(),
                        stationOrder
                )
                .orElseThrow(() ->
                        new StaffBusinessException(
                                "Station order "
                                        + stationOrder
                                        + " does not belong to trip route"
                        )
                );
    }

    private double calculateTicketPrice(
            RouteStation pickup,
            RouteStation dropoff
    ) {
        if (pickup.getPriceFromStart() == null
                || dropoff.getPriceFromStart() == null) {
            throw new StaffBusinessException(
                    "Route station price is missing"
            );
        }

        double price =
                dropoff.getPriceFromStart()
                        - pickup.getPriceFromStart();

        if (price <= 0) {
            throw new StaffBusinessException(
                    "Calculated ticket price must be greater than 0"
            );
        }

        return price;
    }

    private List<String> normalizeAndValidateSeatCodes(
            List<StaffPassengerForm> passengers
    ) {
        List<String> seatCodes =
                passengers.stream()
                        .map(
                                StaffPassengerForm
                                        ::seatCode
                        )
                        .map(this::normalizeSeatCode)
                        .toList();

        Set<String> uniqueCodes =
                new LinkedHashSet<>(seatCodes);

        if (uniqueCodes.size() != seatCodes.size()) {
            throw new StaffBusinessException(
                    "Duplicate seat code in booking request"
            );
        }

        return new ArrayList<>(uniqueCodes);
    }

    private void validateRequestedSeatSegments(
            List<String> requestedSeatCodes,
            List<SeatAvailability> lockedSegments
    ) {
        Set<String> foundCodes =
                lockedSegments.stream()
                        .map(
                                SeatAvailability
                                        ::getSeatCode
                        )
                        .collect(Collectors.toSet());

        List<String> missingCodes =
                requestedSeatCodes.stream()
                        .filter(code ->
                                !foundCodes.contains(code)
                        )
                        .toList();

        if (!missingCodes.isEmpty()) {
            throw new StaffBusinessException(
                    "Seat does not exist for selected trip/segment: "
                            + String.join(", ", missingCodes)
            );
        }
    }

    private void validateSeatsAreAvailable(
            List<String> requestedSeatCodes,
            List<SeatAvailability> segments
    ) {
        Map<String, List<SeatAvailability>> grouped =
                segments.stream()
                        .collect(
                                Collectors.groupingBy(
                                        SeatAvailability
                                                ::getSeatCode
                                )
                        );

        for (String seatCode : requestedSeatCodes) {
            List<SeatAvailability> seatSegments =
                    grouped.getOrDefault(
                            seatCode,
                            Collections.emptyList()
                    );

            boolean unavailable =
                    seatSegments.stream().anyMatch(segment ->
                            segment.getStatus()
                                    != SeatStatus.AVAILABLE
                    );

            if (unavailable) {
                throw new StaffBusinessException(
                        "Seat "
                                + seatCode
                                + " is no longer available"
                );
            }
        }
    }

    // =========================================================
    // PRIVATE: BOOKING/TICKET/SEAT
    // =========================================================

    private List<BookingDetail> getBookingDetails(
            Booking booking
    ) {
        if (booking.getDetails() != null
                && !booking.getDetails().isEmpty()) {
            return booking.getDetails();
        }

        return bookingDetailRepo
                .findByBookingId(booking.getId());
    }

    private void issueTickets(
            List<BookingDetail> details
    ) {
        LocalDateTime now = LocalDateTime.now();

        List<Ticket> tickets = new ArrayList<>();

        for (BookingDetail detail : details) {
            Optional<Ticket> existing =
                    ticketRepo.findByBookingDetailId(
                            detail.getId()
                    );

            if (existing.isPresent()) {
                continue;
            }

            Ticket ticket = new Ticket();
            ticket.setTicketCode(generateTicketCode());
            ticket.setStatus(TicketStatus.VALID);
            ticket.setCreatedAt(now);
            ticket.setBookingDetail(detail);

            tickets.add(ticket);
        }

        if (!tickets.isEmpty()) {
            ticketRepo.saveAll(tickets);
        }
    }

    private void releaseBookingSeats(
            Booking booking
    ) {
        List<BookingDetail> details =
                getBookingDetails(booking);

        List<UUID> detailIds = details.stream()
                .map(BookingDetail::getId)
                .filter(Objects::nonNull)
                .toList();

        if (detailIds.isEmpty()) {
            return;
        }

        List<SeatAvailability> seats =
                seatAvailabilityRepo
                        .findByBookingDetailIds(
                                detailIds
                        );

        seats.forEach(
                SeatAvailability::release
        );

        seatAvailabilityRepo.saveAll(seats);
    }

    private void expireBookingAndReleaseSeats(
            Booking booking
    ) {
        releaseBookingSeats(booking);

        booking.setStatus(BookingStatus.EXPIRED);
        bookingRepo.save(booking);
    }

    private void releaseExpiredSegmentsInMemory(
            List<SeatAvailability> segments,
            LocalDateTime now
    ) {
        segments.stream()
                .filter(segment ->
                        segment.isHoldExpired(now)
                )
                .forEach(
                        SeatAvailability::release
                );
    }

    // =========================================================
    // PRIVATE: VIEW MODEL
    // =========================================================

    private StaffTripViewModel toTripViewModel(
            Trip trip
    ) {
        List<StaffSeatViewModel> seats =
                aggregateSeatStatuses(
                        seatAvailabilityRepo
                                .findOverlappingSegments(
                                        trip.getId(),
                                        0,
                                        Integer.MAX_VALUE
                                )
                );

        long availableCount =
                seats.stream()
                        .filter(seat ->
                                seat.status()
                                        == SeatStatus.AVAILABLE
                        )
                        .count();

        String routeName =
                trip.getRoute() == null
                        ? null
                        : trip.getRoute().getName();

        String busName =
                trip.getBus() == null
                        ? null
                        : trip.getBus().getBusName();

        String licensePlate =
                trip.getBus() == null
                        ? null
                        : trip.getBus().getLicensePlate();

        return new StaffTripViewModel(
                trip.getId(),
                trip.getDestinationFrom(),
                trip.getDestinationTo(),
                trip.getDepartureTime(),
                routeName,
                busName,
                licensePlate,
                availableCount
        );
    }

    private List<StaffSeatViewModel>
    aggregateSeatStatuses(
            List<SeatAvailability> segments
    ) {
        LocalDateTime now = LocalDateTime.now();

        Map<String, List<SeatAvailability>> grouped =
                segments.stream()
                        .collect(
                                Collectors.groupingBy(
                                        SeatAvailability
                                                ::getSeatCode,
                                        TreeMap::new,
                                        Collectors.toList()
                                )
                        );

        List<StaffSeatViewModel> result =
                new ArrayList<>();

        for (Map.Entry<String, List<SeatAvailability>>
                entry : grouped.entrySet()) {

            SeatStatus aggregateStatus =
                    SeatStatus.AVAILABLE;

            LocalDateTime holdExpiredAt = null;

            for (SeatAvailability segment
                    : entry.getValue()) {

                if (segment.getStatus()
                        == SeatStatus.BOOKED) {
                    aggregateStatus =
                            SeatStatus.BOOKED;
                    holdExpiredAt = null;
                    break;
                }

                if (segment.getStatus()
                        == SeatStatus.HELD
                        && !segment.isHoldExpired(now)) {
                    aggregateStatus =
                            SeatStatus.HELD;

                    if (holdExpiredAt == null
                            || (
                            segment.getHoldExpiredAt()
                                    != null
                                    && segment
                                    .getHoldExpiredAt()
                                    .isAfter(
                                            holdExpiredAt
                                    )
                    )) {
                        holdExpiredAt =
                                segment.getHoldExpiredAt();
                    }
                }
            }

            result.add(
                    new StaffSeatViewModel(
                            entry.getKey(),
                            aggregateStatus,
                            holdExpiredAt
                    )
            );
        }

        return result;
    }

    private StaffBookingViewModel toBookingViewModel(
            Booking booking
    ) {
        List<BookingDetail> details =
                getBookingDetails(booking);

        Map<UUID, Ticket> ticketByDetail =
                ticketRepo.findByBookingId(
                                booking.getId()
                        )
                        .stream()
                        .filter(ticket ->
                                ticket.getBookingDetail()
                                        != null
                        )
                        .collect(
                                Collectors.toMap(
                                        ticket ->
                                                ticket
                                                        .getBookingDetail()
                                                        .getId(),
                                        Function.identity(),
                                        (first, second) ->
                                                first
                                )
                        );

        List<StaffBookingDetailViewModel>
                detailViewModels =
                details.stream()
                        .map(detail -> {
                            Ticket ticket =
                                    ticketByDetail.get(
                                            detail.getId()
                                    );

                            return new
                                    StaffBookingDetailViewModel(
                                    detail.getId(),
                                    detail.getPassengerName(),
                                    detail.getSeatCode(),
                                    detail.getTicketPrice(),
                                    detail.getLuggageFee(),
                                    detail.getSubTotal(),
                                    detail.getPickupStationOrder(),
                                    detail.getDropoffStationOrder(),
                                    ticket == null
                                            ? null
                                            : ticket
                                              .getTicketCode(),
                                    ticket == null
                                            ? null
                                            : ticket.getStatus()
                            );
                        })
                        .toList();

        Trip trip = booking.getTrip();

        return new StaffBookingViewModel(
                booking.getId(),
                booking.getBookingCode(),
                booking.getDateBooked(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getContactName(),
                booking.getContactPhone(),
                booking.getContactEmail(),
                trip == null ? null : trip.getId(),
                trip == null
                        ? null
                        : trip.getDepartureTime(),
                detailViewModels
        );
    }

    // =========================================================
    // PRIVATE: LOG/CODE/UTILITY
    // =========================================================

    private void saveActionLog(
            Account staff,
            Booking booking,
            Ticket ticket,
            StaffActionType actionType,
            String description
    ) {
        StaffActionLog log =
                new StaffActionLog();

        log.setStaff(staff);
        log.setBooking(booking);
        log.setTicket(ticket);
        log.setActionType(actionType);
        log.setDescription(description);
        log.setCreatedAt(LocalDateTime.now());

        staffActionLogRepo.save(log);
    }

    private String generateBookingCode() {
        return "BK-"
                + LocalDateTime.now()
                .format(CODE_DATE_FORMAT)
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }

    private String generateTicketCode() {
        return "TK-"
                + LocalDateTime.now()
                .format(CODE_DATE_FORMAT)
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String normalizeSeatCode(
            String seatCode
    ) {
        if (seatCode == null
                || seatCode.isBlank()) {
            throw new StaffBusinessException(
                    "Seat code can not be blank"
            );
        }

        String normalized =
                seatCode.trim().toUpperCase();

        if (!normalized.matches("^[A-Z]\\d{2}$")) {
            throw new StaffBusinessException(
                    "Seat code must match format A01"
            );
        }

        return normalized;
    }

    private String normalizeNullable(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private double normalizeNonNegative(
            Double value
    ) {
        if (value == null) {
            return 0.0;
        }

        if (value < 0) {
            throw new StaffBusinessException(
                    "Value can not be negative"
            );
        }

        return value;
    }

    private String appendNote(
            String currentNote,
            String newNote
    ) {
        if (currentNote == null
                || currentNote.isBlank()) {
            return newNote;
        }

        return currentNote + " | " + newNote;
    }

    private record StationValidationResult(
            RouteStation pickupStation,
            RouteStation dropoffStation
    ) {
    }
}