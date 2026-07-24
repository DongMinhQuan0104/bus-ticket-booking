package com.group8.hsf302.bus_ticket_booking.Infrastructure.Seed;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.*;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Account.AccountJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking.BookingJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.BookingDetail.BookingDetailJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Bus.BusJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment.PaymentJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Review.ReviewJpaRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Transaction.TransactionJpaRepo;
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
import java.util.ArrayList;
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
    private final ReviewJpaRepo reviewRepo;
    private final TransactionJpaRepo transactionRepo;
    private final PasswordHasher passwordHasher;

    public DataSeeder(AccountJpaRepo accountRepo, BusJpaRepo busRepo, StationJpaRepo stationRepo,
                      RouteJpaRepo routeRepo, RouteStationJpaRepo routeStationRepo, TripJpaRepo tripRepo,
                      BookingJpaRepo bookingRepo, BookingDetailJpaRepo bookingDetailRepo,
                      SeatAvailabilityJpaRepo seatRepo, PaymentJpaRepo paymentRepo,
                      ReviewJpaRepo reviewRepo, TransactionJpaRepo transactionRepo, PasswordHasher passwordHasher) {
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
        this.reviewRepo = reviewRepo;
        this.transactionRepo = transactionRepo;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(String... args) {
        if (accountRepo.count() > 0) {
            return; // Da co du lieu -> khong seed lai
        }

        String pwd = passwordHasher.hash("123456");

        // ===== 1. Tai khoan (1 admin, 3 tai xe, 4 khach hang) =====
        Account admin = new Account(Status.AVAILABLE, "0900000001", pwd, "admin@bus.com", Role.ADMIN, "Quản Trị Viên");

        Account taiTai = new Account(Status.AVAILABLE, "0911000001", pwd, "tai@bus.com", Role.DRIVER, "Trần Văn Tài");
        Account taiHung = new Account(Status.AVAILABLE, "0911000002", pwd, "hung@bus.com", Role.DRIVER, "Lê Mạnh Hùng");
        Account taiNam = new Account(Status.AVAILABLE, "0911000003", pwd, "nam@bus.com", Role.DRIVER, "Phạm Hoài Nam");

        Account khach = new Account(Status.AVAILABLE, "0922000001", pwd, "khach@bus.com", Role.CUSTOMER, "Nguyễn Văn Khách");
        Account lan = new Account(Status.AVAILABLE, "0922000002", pwd, "lan@bus.com", Role.CUSTOMER, "Trần Thị Lan");
        Account minh = new Account(Status.AVAILABLE, "0922000003", pwd, "minh@bus.com", Role.CUSTOMER, "Hoàng Đức Minh");
        Account thao = new Account(Status.AVAILABLE, "0922000004", pwd, "thao@bus.com", Role.CUSTOMER, "Vũ Phương Thảo");

        accountRepo.saveAll(List.of(admin, taiTai, taiHung, taiNam, khach, lan, minh, thao));

        // ===== 2. Xe (6 xe, ca 16 va 32 cho) =====
        Bus bus1 = new Bus("Limousine 16 chỗ", "29B-111.11", BusType.SEATED, BusCapacity.SEAT_16, Status.AVAILABLE);
        Bus bus2 = new Bus("Giường nằm 32 chỗ", "29B-222.22", BusType.SLEEPER, BusCapacity.SEAT_32, Status.AVAILABLE);
        Bus bus3 = new Bus("Thaco Mobihome", "51B-333.33", BusType.SLEEPER, BusCapacity.SEAT_32, Status.AVAILABLE);
        Bus bus4 = new Bus("Hyundai Solati", "51B-444.44", BusType.SEATED, BusCapacity.SEAT_16, Status.AVAILABLE);
        Bus bus5 = new Bus("Ford Transit", "43B-555.55", BusType.SEATED, BusCapacity.SEAT_16, Status.AVAILABLE);
        Bus bus6 = new Bus("Xe bảo trì", "43B-666.66", BusType.SLEEPER, BusCapacity.SEAT_32, Status.NOT_AVAILABLE);
        busRepo.saveAll(List.of(bus1, bus2, bus3, bus4, bus5, bus6));

        // ===== 3. Ben xe =====
        Station stHaNoi = station("Bến xe Mỹ Đình", "Hà Nội");
        Station stNinhBinh = station("Bến xe Ninh Bình", "Ninh Bình");
        Station stThanhHoa = station("Bến xe Thanh Hóa", "Thanh Hóa");
        Station stVinh = station("Bến xe Vinh", "Nghệ An");
        Station stHue = station("Bến xe Huế", "Thừa Thiên Huế");
        Station stDaNang = station("Bến xe Đà Nẵng", "Đà Nẵng");
        Station stHaiPhong = station("Bến xe Niệm Nghĩa", "Hải Phòng");
        Station stSaiGon = station("Bến xe Miền Đông", "Sài Gòn");
        stationRepo.saveAll(List.of(stHaNoi, stNinhBinh, stThanhHoa, stVinh, stHue, stDaNang, stHaiPhong, stSaiGon));

        // ===== 4. Tuyen + cac diem dung =====
        Route rHaNoiVinh = route("Hà Nội - Vinh",
                stop(stHaNoi, 1, 0.0), stop(stNinhBinh, 2, 100000.0), stop(stThanhHoa, 3, 170000.0), stop(stVinh, 4, 250000.0));
        Route rHaNoiHue = route("Hà Nội - Huế",
                stop(stHaNoi, 1, 0.0), stop(stVinh, 2, 250000.0), stop(stHue, 3, 400000.0));
        Route rHaNoiDaNang = route("Hà Nội - Đà Nẵng",
                stop(stHaNoi, 1, 0.0), stop(stHue, 2, 400000.0), stop(stDaNang, 3, 480000.0));
        Route rHaNoiHaiPhong = route("Hà Nội - Hải Phòng",
                stop(stHaNoi, 1, 0.0), stop(stHaiPhong, 2, 120000.0));
        Route rDaNangSaiGon = route("Đà Nẵng - Sài Gòn",
                stop(stDaNang, 1, 0.0), stop(stSaiGon, 2, 550000.0));
        routeRepo.saveAll(List.of(rHaNoiVinh, rHaNoiHue, rHaNoiDaNang, rHaNoiHaiPhong, rDaNangSaiGon));

        // ===== 5. Chuyen xe: nhieu ngay, nhieu tuyen, nhieu tai xe =====
        LocalDateTime d1 = LocalDateTime.now().plusDays(1).withHour(6).withMinute(0).withSecond(0).withNano(0);
        List<Trip> trips = new ArrayList<>();

        // Ha Noi -> Vinh: 3 khung gio moi ngay, trong 3 ngay toi
        for (int day = 0; day < 3; day++) {
            trips.add(trip("Hà Nội", "Vinh", d1.plusDays(day),               "Trần Văn Tài", 250000.0, TripStatus.SCHEDULED, rHaNoiVinh, bus1));
            trips.add(trip("Hà Nội", "Vinh", d1.plusDays(day).plusHours(6),  "Lê Mạnh Hùng", 250000.0, TripStatus.SCHEDULED, rHaNoiVinh, bus2));
            trips.add(trip("Hà Nội", "Vinh", d1.plusDays(day).plusHours(13), "Phạm Hoài Nam", 280000.0, TripStatus.SCHEDULED, rHaNoiVinh, bus3));
        }
        // Cac tuyen khac
        for (int day = 0; day < 3; day++) {
            trips.add(trip("Hà Nội", "Huế",       d1.plusDays(day).plusHours(2),  "Trần Văn Tài", 400000.0, TripStatus.SCHEDULED, rHaNoiHue, bus2));
            trips.add(trip("Hà Nội", "Đà Nẵng",   d1.plusDays(day).plusHours(4),  "Lê Mạnh Hùng", 480000.0, TripStatus.SCHEDULED, rHaNoiDaNang, bus3));
            trips.add(trip("Hà Nội", "Hải Phòng", d1.plusDays(day).plusHours(8),  "Phạm Hoài Nam", 120000.0, TripStatus.SCHEDULED, rHaNoiHaiPhong, bus4));
            trips.add(trip("Đà Nẵng", "Sài Gòn",  d1.plusDays(day).plusHours(10), "Trần Văn Tài", 550000.0, TripStatus.SCHEDULED, rDaNangSaiGon, bus5));
        }
        // Chuyen sap chay hom nay (de test huy ve <12h -> khong hoan tien)
        Trip soon = trip("Hà Nội", "Vinh", LocalDateTime.now().plusHours(5), "Trần Văn Tài",
                250000.0, TripStatus.SCHEDULED, rHaNoiVinh, bus1);
        trips.add(soon);
        // Chuyen dang chay (tai xe xem duoc danh sach khach)
        Trip running = trip("Hà Nội", "Huế", LocalDateTime.now().minusHours(2), "Trần Văn Tài",
                400000.0, TripStatus.RUNNING, rHaNoiHue, bus2);
        trips.add(running);
        // Chuyen da hoan thanh (de test danh gia E6)
        Trip done1 = trip("Hà Nội", "Vinh", LocalDateTime.now().minusDays(2).withHour(8), "Trần Văn Tài",
                250000.0, TripStatus.COMPLETED, rHaNoiVinh, bus1);
        Trip done2 = trip("Hà Nội", "Đà Nẵng", LocalDateTime.now().minusDays(5).withHour(9), "Lê Mạnh Hùng",
                480000.0, TripStatus.COMPLETED, rHaNoiDaNang, bus3);
        trips.add(done1);
        trips.add(done2);
        tripRepo.saveAll(trips);

        // ===== 6. Ve mau =====
        // Ma ghe phai dung dinh dang ^[A-Z]\d{2}$ (1 chu hoa + 2 chu so), vd A01.
        Trip firstHaNoiVinh = trips.get(0);
        // khach@bus.com: 1 ve sap di (huy duoc, hoan 100%), 1 ve da di (danh gia duoc)
        createBooking(khach, firstHaNoiVinh, List.of("A01", "A02"),
                List.of("Nguyễn Văn Khách", "Lê Thị Hoa"), PaymentMethod.BANK_TRANSFER);
        createBooking(khach, done1, List.of("A03"), List.of("Nguyễn Văn Khách"), PaymentMethod.BANK_TRANSFER);
        // ve chuyen sap chay trong 5h -> test huy khong duoc hoan tien
        createBooking(khach, soon, List.of("A05"), List.of("Nguyễn Văn Khách"), PaymentMethod.BANK_TRANSFER);

        // lan@bus.com: giu san vai ghe de thay ghe da co nguoi dat tren so do
        createBooking(lan, firstHaNoiVinh, List.of("A04"), List.of("Trần Thị Lan"), PaymentMethod.BANK_TRANSFER);
        createBooking(lan, done2, List.of("A07", "A08"),
                List.of("Trần Thị Lan", "Hoàng Đức Minh"), PaymentMethod.BANK_TRANSFER);

        // minh@bus.com: khach tren chuyen DANG CHAY -> tai xe check-in duoc
        createBooking(minh, running, List.of("A10", "A11"),
                List.of("Hoàng Đức Minh", "Vũ Phương Thảo"), PaymentMethod.BANK_TRANSFER);

        // ===== 7. Feedback mau (danh gia chuyen da hoan thanh) =====
        review(khach, done1, 5, "Xe sạch sẽ, tài xế thân thiện, đúng giờ. Rất hài lòng!");
        review(lan, done2, 4, "Chuyến đi thoải mái, ghế rộng. Chỉ hơi trễ 15 phút.");
        review(minh, done2, 3, "Bình thường, điều hòa hơi yếu ở hàng ghế cuối.");

        // ===== 8. Yeu cau hoan tien CHO DUYET mau (khach da huy ve) =====
        pendingRefund(khach, 250000.0);
        pendingRefund(lan, 480000.0);
    }

    /** Tao 1 danh gia mau cho chuyen da hoan thanh. */
    private void review(Account account, Trip trip, int rating, String comment) {
        Review r = new Review();
        r.setAccount(account);
        r.setTrip(trip);
        r.setRating(rating);
        r.setComment(comment);
        r.setCreatedAt(LocalDateTime.now().minusHours(rating));
        reviewRepo.save(r);
    }

    /** Tao 1 yeu cau hoan tien o trang thai PENDING (cho Admin duyet). */
    private void pendingRefund(Account customer, double amount) {
        Transaction tx = new Transaction();
        tx.setTo(customer);
        tx.setAmount(amount);
        tx.setStatus(TransactionStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now().minusMinutes((long) amount % 120));
        transactionRepo.save(tx);
    }

    /** Tao tuyen kem cac diem dung. */
    private Route route(String name, RouteStation... stops) {
        Route r = new Route();
        r.setName(name);
        for (RouteStation s : stops) {
            s.setRoute(r);
            r.getRouteStations().add(s);
        }
        return r;
    }

    /** 1 diem dung (chua gan route - route duoc gan trong route(...)). */
    private RouteStation stop(Station station, int order, double priceFromStart) {
        RouteStation rs = new RouteStation();
        rs.setStation(station);
        rs.setStationOrder(order);
        rs.setPriceFromStart(priceFromStart);
        return rs;
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
