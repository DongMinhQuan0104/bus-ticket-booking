package com.group8.hsf302.bus_ticket_booking.Infrastructure.Seed;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.*;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Account.AccountJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking.BookingJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.BookingDetail.BookingDetailJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Bus.BusJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment.PaymentJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Route.RouteJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.RouteStation.RouteStationJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability.SeatAvailabilityJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Station.StationJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip.TripJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DataSeeder - nap DU LIEU GIA khi database con trong (ddl-auto=create tao lai schema moi lan chay).
 * Muc dich: co san tai khoan 3 role de dang nhap, co chuyen/xe/tuyen/tram va vai ve mau
 * de test toan bo FE->BE (Admin / Driver / Customer) ma khong phai nhap tay.
 *
 * Tai khoan mau (mat khau da hash BCrypt):
 *   - Admin:    admin@bus.com    / 123456
 *   - Driver:   tai@bus.com      / 123456   (fullName "Tran Van Tai" = driverName cua cac chuyen duoc phan)
 *   - Customer: khach@bus.com    / 123456
 *   - Customer: lan@bus.com      / 123456
 *
 * Chi chay khi bang account rong -> khong ghi de neu da co du lieu that.
 */
@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private final AccountJpaRepo accountRepo;
    private final BusJpaRepo busRepo;
    private final StationJpaRepo stationRepo;
    private final RouteJpaRepo routeRepo;
    private final RouteStationJpaRepo routeStationRepo;
    private final TripJpaRepo tripRepo;
    private final BookingJpaRepo bookingRepo;
    private final BookingDetailJpaRepo bookingDetailRepo;
    private final SeatAvailabilityJpaRepo seatRepo;
    private final PaymentJpaRepo paymentRepo;
    private final PasswordHasher passwordHasher;

    public DataSeeder(AccountJpaRepo accountRepo, BusJpaRepo busRepo, StationJpaRepo stationRepo,
                      RouteJpaRepo routeRepo, RouteStationJpaRepo routeStationRepo, TripJpaRepo tripRepo,
                      BookingJpaRepo bookingRepo, BookingDetailJpaRepo bookingDetailRepo,
                      SeatAvailabilityJpaRepo seatRepo, PaymentJpaRepo paymentRepo, PasswordHasher passwordHasher) {
        this.accountRepo = accountRepo;
        this.busRepo = busRepo;
        this.stationRepo = stationRepo;
        this.routeRepo = routeRepo;
        this.routeStationRepo = routeStationRepo;
        this.tripRepo = tripRepo;
        this.bookingRepo = bookingRepo;
        this.bookingDetailRepo = bookingDetailRepo;
        this.seatRepo = seatRepo;
        this.paymentRepo = paymentRepo;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(String... args) {
        if (accountRepo.count() > 0) {
            return; // Da co du lieu -> khong seed lai
        }

        String pwd = passwordHasher.hash("123456");

        // ===== 1. Tai khoan 3 role =====
        Account admin = new Account(Status.AVAILABLE, "0900000001", pwd, "admin@bus.com", Role.ADMIN, "Quan Tri Vien");
        Account driver = new Account(Status.AVAILABLE, "0900000002", pwd, "tai@bus.com", Role.DRIVER, "Tran Van Tai");
        Account khach = new Account(Status.AVAILABLE, "0900000003", pwd, "khach@bus.com", Role.CUSTOMER, "Nguyen Van Khach");
        Account lan = new Account(Status.AVAILABLE, "0900000004", pwd, "lan@bus.com", Role.CUSTOMER, "Tran Thi Lan");
        accountRepo.saveAll(List.of(admin, driver, khach, lan));

        // ===== 2. Xe (16 va 32 cho) =====
        Bus bus16 = new Bus("Limousine 16 cho", "51B-111.11", BusType.SEATED, BusCapacity.SEAT_16, Status.AVAILABLE);
        Bus bus32 = new Bus("Giuong nam 32 cho", "51B-222.22", BusType.SLEEPER, BusCapacity.SEAT_32, Status.AVAILABLE);
        busRepo.saveAll(List.of(bus16, bus32));

        // ===== 3. Tram =====
        Station stHaNoi = station("Ben xe My Dinh", "Ha Noi");
        Station stNinhBinh = station("Ben xe Ninh Binh", "Ninh Binh");
        Station stVinh = station("Ben xe Vinh", "Nghe An");
        Station stHue = station("Ben xe Hue", "Thua Thien Hue");
        stationRepo.saveAll(List.of(stHaNoi, stNinhBinh, stVinh, stHue));

        // ===== 4. Tuyen + cac tram tren tuyen =====
        Route routeBac = new Route();
        routeBac.setName("Ha Noi - Vinh");
        routeBac.getRouteStations().add(routeStation(routeBac, stHaNoi, 1, 0.0));
        routeBac.getRouteStations().add(routeStation(routeBac, stNinhBinh, 2, 120000.0));
        routeBac.getRouteStations().add(routeStation(routeBac, stVinh, 3, 250000.0));
        routeRepo.save(routeBac);

        Route routeTrung = new Route();
        routeTrung.setName("Ha Noi - Hue");
        routeTrung.getRouteStations().add(routeStation(routeTrung, stHaNoi, 1, 0.0));
        routeTrung.getRouteStations().add(routeStation(routeTrung, stHue, 2, 400000.0));
        routeRepo.save(routeTrung);

        // ===== 5. Chuyen xe =====
        LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(7).withMinute(0).withSecond(0).withNano(0);
        Trip t1 = trip("Ha Noi", "Vinh", base, "Tran Van Tai", 250000.0, TripStatus.SCHEDULED, routeBac, bus16);
        Trip t2 = trip("Ha Noi", "Vinh", base.plusHours(5), "Tran Van Tai", 250000.0, TripStatus.SCHEDULED, routeBac, bus32);
        Trip t3 = trip("Ha Noi", "Hue", base.plusDays(1), "Tran Van Tai", 400000.0, TripStatus.SCHEDULED, routeTrung, bus32);
        Trip t4 = trip("Ha Noi", "Vinh", LocalDateTime.now().minusDays(2).withHour(8), "Tran Van Tai",
                250000.0, TripStatus.COMPLETED, routeBac, bus16); // chuyen da hoan thanh -> danh gia (E6)
        tripRepo.saveAll(List.of(t1, t2, t3, t4));

        // ===== 6. Ve mau cho khach hang (dung dung object-graph nhu createBooking) =====
        // Ve 1: chuyen sap toi t1, 2 ghe -> hien o "Ve cua toi", co the huy (E5)
        // Ma ghe phai dung dinh dang ^[A-Z]\d{2}$ (1 chu hoa + 2 chu so), vd A01.
        createBooking(khach, t1, List.of("A01", "A02"), List.of("Nguyen Van Khach", "Le Thi Hoa"), PaymentMethod.COD);
        // Ve 2: chuyen da hoan thanh t4 -> co the danh gia (E6)
        createBooking(khach, t4, List.of("A03"), List.of("Nguyen Van Khach"), PaymentMethod.CASH);
    }

    // ---- helper ----

    private Station station(String name, String address) {
        Station s = new Station();
        s.setName(name);
        s.setAddress(address);
        s.setStatus(Status.AVAILABLE);
        return s;
    }

    private RouteStation routeStation(Route route, Station station, int order, double priceFromStart) {
        RouteStation rs = new RouteStation();
        rs.setRoute(route);
        rs.setStation(station);
        rs.setStationOrder(order);
        rs.setPriceFromStart(priceFromStart);
        return rs;
    }

    private Trip trip(String from, String to, LocalDateTime departure, String driverName,
                      double price, TripStatus status, Route route, Bus bus) {
        Trip t = new Trip(from, to, departure, driverName, status, route, bus);
        t.setPrice(price);
        return t;
    }

    private void createBooking(Account account, Trip trip, List<String> seatCodes,
                               List<String> passengerNames, PaymentMethod method) {
        double unit = trip.getPrice() != null ? trip.getPrice() : 0.0;
        Booking booking = new Booking();
        booking.setDateBooked(LocalDateTime.now());
        booking.setTotalPrice(unit * seatCodes.size());
        booking.setNote(null);
        booking.setBookingType(BookingType.ONEWAY);
        booking.setAccount(account);
        bookingRepo.save(booking);

        for (int i = 0; i < seatCodes.size(); i++) {
            BookingDetail detail = new BookingDetail();
            detail.setPassengerName(passengerNames.get(i));
            detail.setTicketPrice(unit);
            detail.setLuggageWeightKg(0.0);
            detail.setLuggageFee(0.0);
            detail.setSubTotal(unit);
            detail.setReturnTicket(false);
            detail.setIsCheckedIn(false);
            detail.setBooking(booking);
            bookingDetailRepo.save(detail);

            SeatAvailability seat = new SeatAvailability();
            seat.setSeatCode(seatCodes.get(i));
            seat.setStartStationOrder(0);
            seat.setEndStationOrder(0);
            seat.setBookingDetail(detail);
            seat.setTrip(trip);
            seatRepo.save(seat);
        }

        Payment payment = new Payment();
        payment.setCreatePayment(LocalDateTime.now());
        payment.setPaymentMethod(method);
        payment.setBooking(booking);
        paymentRepo.save(payment);
    }
}
