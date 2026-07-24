# 🗺️ Bản đồ định vị chức năng — Bus Ticket Booking

> **Cách dùng khi thầy vấn đáp:** Thầy đang xem trang nào → tìm mục trang đó bên dưới → bấm link để mở đúng dòng code.
> Mỗi chức năng đi theo luồng: **Trang (HTML)** → **Controller** (nhận request) → **Service** (xử lý logic) → **Repository** (truy vấn DB).

## 🔁 Một request chạy qua đâu?

```
Trình duyệt  ─►  Controller (Presentation)  ─►  Service (Application)  ─►  Repository (Domain/Infrastructure)  ─►  SQL Server
   ▲                                                                                                                    │
   └───────────────────────  Template Thymeleaf (.html)  ◄── Model dữ liệu ◄──────────────────────────────────────────┘
```

- **Controller** = "trang này gọi hàm nào". Nằm ở `src/main/java/.../Presentation/Controller/`
- **Service** = "logic nghiệp vụ thật sự". Interface + `...Impl` ở `.../Application/Service/`
- **Template** = giao diện, ở `src/main/resources/templates/`

---

## 📇 Tra nhanh: "Tôi đang ở trang…"

| Trang đang mở trên trình duyệt | URL | Nhảy tới mục |
|---|---|---|
| Trang chủ (Home) | `/home` | [1. Trang chung](#1-trang-chung-authhome) |
| Đăng nhập | `/auth/login` | [1. Trang chung](#1-trang-chung-authhome) |
| Đăng ký | `/auth/register` | [1. Trang chung](#1-trang-chung-authhome) |
| Tìm chuyến / kết quả tìm | `/trips/search` | [2. Customer](#2-customer) |
| Chọn ghế | `/trips/{id}/booking` | [2. Customer](#2-customer) |
| Thanh toán | `/bookings/checkout` | [2. Customer](#2-customer) |
| Vé của tôi | `/my-tickets` | [2. Customer](#2-customer) |
| Hủy vé / hoàn tiền | `/my-tickets/{id}/cancel` | [2. Customer](#2-customer) |
| Đánh giá chuyến | `/my-tickets/{id}/review` | [2. Customer](#2-customer) |
| Hồ sơ khách | `/profile` | [2. Customer](#2-customer) |
| Chuyến của tài xế | `/driver/trips` | [3. Driver](#3-driver) |
| Danh sách hành khách | `/driver/trips/{id}/passengers` | [3. Driver](#3-driver) |
| Trang quản trị (mọi trang `/admin/*`) | `/admin/...` | [4. Admin](#4-admin) |

---

## 1. TRANG CHUNG (auth/home)

### 🏠 Trang chủ (Home)
- **Giao diện:** [auth/home.html](src/main/resources/templates/auth/home.html)
- **Controller:** [HomeController.showHome()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/HomeController.java#L26) — `GET /home`
- Ô Điểm đi/đến (dropdown) lấy từ [CustomerServiceImpl.getDepartureCities()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L218) / [getArrivalCities()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L224)

### 🔑 Đăng nhập (Login)
- **Giao diện:** [auth/login.html](src/main/resources/templates/auth/login.html)
- **Controller:** hiện form [showLoginForm()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L31) `GET /auth/login` · xử lý đăng nhập [processLogin()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L55) `POST /auth/login`
- **Logic:** [AuthenticationServiceImpl.login()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Authentication/AuthenticationServiceImpl.java#L43) — so khớp mật khẩu (BCrypt)
- **Điều hướng theo role** (Admin/Driver/Customer) nằm trong `redirectByRole()` cùng file [AuthenticationController](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L55)

### 📝 Đăng ký (Register)
- **Giao diện:** [auth/register.html](src/main/resources/templates/auth/register.html)
- **Controller:** [showRegisterForm()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L39) `GET` · [processRegister()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L86) `POST /auth/register`
- **Logic:** [AuthenticationServiceImpl.register()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Authentication/AuthenticationServiceImpl.java#L28)

### 🚪 Đăng xuất (Logout)
- [AuthenticationController.logout()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AuthenticationController.java#L47) — `GET /auth/logout` (xóa session)

---

## 2. CUSTOMER

### 🔍 Tìm chuyến xe
- **Giao diện:** [customer/trip-search.html](src/main/resources/templates/customer/trip-search.html)
- **Controller:** [CustomerTripController.search()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerTripController.java#L49) — `GET /trips/search`
- **Logic tìm kiếm:** [CustomerServiceImpl.searchTrips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L148)

### 💺 Chọn ghế
- **Giao diện:** [customer/trip-booking.html](src/main/resources/templates/customer/trip-booking.html)
- **Controller:** [CustomerTripController.bookingPage()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerTripController.java#L89) — `GET /trips/{tripId}/booking` (chặn khách chưa đăng nhập ở đây)
- **Logic:** [getTripForBooking()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L180) · [getOccupiedSeatCodes()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L192)

### 💳 Thanh toán + Giữ ghế tạm (timeout)
- **Giao diện:** [customer/booking-checkout.html](src/main/resources/templates/customer/booking-checkout.html) (có đồng hồ đếm ngược)
- **Controller:** vào trang [BookingController.checkout()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/BookingController.java#L49) `GET /bookings/checkout` · xác nhận [confirm()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/BookingController.java#L108) `POST /bookings/confirm`
- **Giữ ghế 10 phút:** [CustomerServiceImpl.holdSeats()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L239)
- **Tạo vé:** [createBooking()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L282)
- **Tự động nhả ghế hết hạn:** [SeatHoldCleanupScheduler](src/main/java/com/group8/hsf302/bus_ticket_booking/Infrastructure/Scheduler/SeatHoldCleanupScheduler.java#L25) → [releaseExpiredSeatHolds()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L276)
- **Trạng thái ghế giữ/tạm:** [SeatAvailability.isActiveHold()](src/main/java/com/group8/hsf302/bus_ticket_booking/Domain/Model/SeatAvailability.java#L74) · [confirmHold()](src/main/java/com/group8/hsf302/bus_ticket_booking/Domain/Model/SeatAvailability.java#L79)

### 🎫 Vé của tôi
- **Giao diện:** [customer/my-tickets.html](src/main/resources/templates/customer/my-tickets.html)
- **Controller:** [MyTicketController.myTickets()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/MyTicketController.java#L44) — `GET /my-tickets`
- **Logic:** [getMyBookings()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L372)

### ↩️ Hủy vé + Hoàn tiền
- **Giao diện:** [customer/booking-cancel.html](src/main/resources/templates/customer/booking-cancel.html) (hiện % hoàn theo chính sách)
- **Controller:** xem trước [cancelForm()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/MyTicketController.java#L61) `GET` · thực hiện [doCancel()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/MyTicketController.java#L85) `POST /my-tickets/{id}/cancel`
- **Logic tính hoàn:** [previewRefund()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L432) · [cancelBooking()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L440) (ghi yêu cầu hoàn tiền PENDING chờ Admin duyệt). Chính sách 24h/12h nằm ngay trong `calculateRefund()` cùng file.

### ⭐ Đánh giá chuyến
- **Giao diện:** [customer/booking-review.html](src/main/resources/templates/customer/booking-review.html)
- **Controller:** [reviewForm()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/MyTicketController.java#L104) `GET` · [doReview()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/MyTicketController.java#L122) `POST /my-tickets/{id}/review`
- **Logic:** [reviewBooking()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Customer/CustomerServiceImpl.java#L483)

### 👤 Hồ sơ khách
- **Giao diện:** [customer/profile.html](src/main/resources/templates/customer/profile.html)
- **Controller:** [profile()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerController.java#L41) `GET /profile` · [update()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerController.java#L61) · [changePassword()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerController.java#L82) · [deactivate()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/CustomerController.java#L97)

---

## 3. DRIVER

### 🚌 Danh sách chuyến được phân
- **Giao diện:** [driver/trips.html](src/main/resources/templates/driver/trips.html)
- **Controller:** [DriverController.showAssignedTrips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/DriverController.java#L43) — `GET /driver/trips`
- **Logic:** [DriverServiceImpl.getAssignedTrips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Driver/DriverServiceImpl.java#L35)

### 🧾 Danh sách hành khách (manifest)
- **Giao diện:** [driver/passengers.html](src/main/resources/templates/driver/passengers.html)
- **Controller:** [showPassengerManifest()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/DriverController.java#L52) — `GET /driver/trips/{id}/passengers`
- **Logic:** [getPassengerManifest()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Driver/DriverServiceImpl.java#L70)

### 🔄 Đổi trạng thái chuyến (SCHEDULED → RUNNING → COMPLETED)
- **Controller:** [updateTripStatus()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/DriverController.java#L63) — `POST /driver/trips/{id}/status`
- **Logic:** [DriverServiceImpl.updateTripStatus()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Driver/DriverServiceImpl.java#L50)

### ✅ Check-in hành khách
- **Controller:** [checkInPassenger()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/DriverController.java#L86) — `POST /driver/tickets/{id}/checkin`
- **Logic:** [DriverServiceImpl.checkInPassenger()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Driver/DriverServiceImpl.java#L86)

### 👤 Hồ sơ tài xế
- [driver/profile.html](src/main/resources/templates/driver/profile.html) · [showDriverProfile()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/DriverController.java#L79) `GET /driver/profile`

---

## 4. ADMIN

> Tất cả trang admin dùng chung layout [admin/layout.html](src/main/resources/templates/admin/layout.html) (sidebar/topbar). Mọi endpoint kiểm tra quyền ADMIN qua `verifyAdminAuth()` trong [AdminController](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java).

### 📊 Dashboard
- **Giao diện:** [admin/dashboard.html](src/main/resources/templates/admin/dashboard.html)
- **Controller:** [AdminController.dashboard()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L70) — `GET /admin/dashboard`
- **Chuyến đang hoạt động (phân trang):** [getActiveTrips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L456)

### 👥 Quản lý tài khoản
- **Giao diện:** [admin/accounts.html](src/main/resources/templates/admin/accounts.html)
- **Controller:** danh sách [accounts()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L160) · thêm [createAccount()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L176) · sửa [updateAccount()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L184) · xóa [deleteAccount()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L193)

### 🚍 Quản lý chuyến xe
- **Giao diện:** [admin/trips.html](src/main/resources/templates/admin/trips.html)
- **Controller:** [trips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L279) · [createTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L291) · [updateTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L299) · [deleteTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L307)
- **Logic:** [createTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L374) · [getAllTrips()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L386) · [updateTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L431) · [deletedTrip()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L445)

### 🚐 Quản lý xe (bus)
- **Giao diện:** [admin/buses.html](src/main/resources/templates/admin/buses.html)
- **Controller:** [buses()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L117) · [createBus()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L134) · [updateBus()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L142) · [deleteBus()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L150)

### 🛣️ Quản lý tuyến (kèm sửa điểm dừng)
- **Giao diện:** [admin/routes.html](src/main/resources/templates/admin/routes.html) — modal sửa nạp sẵn các điểm dừng hiện tại
- **Controller:** [routes()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L202) · [createRoute()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L211) · [updateRoute()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L219) · [deleteRoute()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L228)
- **Logic:** [createRoute()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L211) · [updateRoute()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L269) (xử lý các điểm dừng qua `processRouteStations()` cùng file)

### 📍 Quản lý bến (station)
- **Giao diện:** [admin/stations.html](src/main/resources/templates/admin/stations.html)
- **Controller:** [stations()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L238) · [createStation()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L252) · [updateStation()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L260) · [deleteStation()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L268)

### 💰 Duyệt hoàn tiền
- **Giao diện:** [admin/refunds.html](src/main/resources/templates/admin/refunds.html)
- **Controller:** danh sách [refunds()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L90) · duyệt [approveRefund()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L98) `POST /admin/refunds/{id}/approve`
- **Logic:** [getPendingRefunds()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L475) · [approveRefund()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L493) (chuyển PENDING → PAID)

### 💬 Đánh giá của khách (feedback)
- **Giao diện:** [admin/reviews.html](src/main/resources/templates/admin/reviews.html)
- **Controller:** [reviews()](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Controller/AdminController.java#L108) — `GET /admin/reviews`
- **Logic:** [getAllReviews()](src/main/java/com/group8/hsf302/bus_ticket_booking/Application/Service/Admin/AdminServiceImpl.java#L517)

---

## 🧩 Thành phần dùng chung (thầy hay hỏi thêm)

| Thành phần | Vị trí | Vai trò |
|---|---|---|
| Xử lý lỗi tập trung | [GlobalExceptionHandler](src/main/java/com/group8/hsf302/bus_ticket_booking/Presentation/Exception/GlobalExceptionHandler.java) | Bắt exception → flash message → redirect (mẫu PRG) |
| Dữ liệu mẫu (tài khoản, chuyến…) | [DataSeeder](src/main/java/com/group8/hsf302/bus_ticket_booking/Infrastructure/Seed/DataSeeder.java) | Nạp dữ liệu khi DB trống |
| Mã hóa mật khẩu | [BCryptPasswordHasher](src/main/java/com/group8/hsf302/bus_ticket_booking/Infrastructure/Security) | Hash mật khẩu (jBCrypt) |
| Cấu hình DB / cổng | [application.properties](src/main/resources/application.properties) | Kết nối SQL Server, `ddl-auto=create` |
| Các thực thể (Entity) | [Domain/Model/](src/main/java/com/group8/hsf302/bus_ticket_booking/Domain/Model) | Account, Trip, Booking, SeatAvailability, Review, Transaction… |
| Enum trạng thái | [Domain/Enum/](src/main/java/com/group8/hsf302/bus_ticket_booking/Domain/Enum) | TripStatus, Role, PaymentMethod, TransactionStatus… |

### 🔑 Tài khoản demo (mật khẩu đều là `123456`)
| Role | Email |
|---|---|
| Admin | `admin@bus.com` |
| Driver | `tai@bus.com` |
| Customer | `khach@bus.com` |

---

## 📐 Quy tắc đọc code (mẹo trả lời thầy)
1. **"Chức năng X nằm đâu?"** → Trang → **Controller** (điểm vào) → **Service `...Impl`** (logic chính, thường là chỗ thầy muốn xem).
2. **Controller** chỉ điều phối (nhận request, gọi service, trả view). **Không** chứa logic nghiệp vụ.
3. **Service** interface khai báo, `...Impl` cài đặt — mở file `...Impl` để xem logic thật.
4. **Repository**: interface ở `Domain/Repository`, cài đặt ở `Infrastructure/Persistence/.../...Impl` (bọc quanh Spring Data JPA).
5. Dữ liệu trả ra giao diện là các **ViewModel** (record) ở `Application/Dto/Response`; dữ liệu nhận vào là các **Form** ở `Application/Dto/Request`.
