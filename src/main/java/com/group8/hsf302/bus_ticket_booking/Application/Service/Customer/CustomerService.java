package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateReviewForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.RefundViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service nghiep vu cho vai tro Customer (khach hang).
 * <p>
 * Ngoai cac chuc nang tai khoan co san (getAccount/update/changePassword/deleted),
 * lop nay duoc bo sung toan bo luong dat ve E1 -> E6 theo FunctionList:
 * <ul>
 *   <li>E1: tim kiem chuyen xe</li>
 *   <li>E2: xem so do ghe de chon</li>
 *   <li>E3: tao booking va thanh toan</li>
 *   <li>E4: xem ve cua toi</li>
 *   <li>E5: huy ve / hoan tien</li>
 *   <li>E6: danh gia chuyen di</li>
 * </ul>
 * Toan bo kiem tra nghiep vu (vd: diem di != diem den, ghe con trong, quyen so huu ve)
 * deu nam o day - tang service, dung nguyen tac "Backend khong tin tuong Frontend".
 */
public interface CustomerService {
    public AccountViewModel getAccount(UUID accountId);
    public AccountViewModel update(UpdateAccountForm form,UUID  accountId);
    public boolean changePassword(ChangePasswordForm form,UUID accountId);
    public boolean deleted(UUID accountId);

    /**
     * E1 - Tim cac chuyen xe kha dung theo diem di, diem den va ngay di.
     * Nem SameStationException neu diem di trung diem den.
     * Moi ket qua da duoc tinh san so ghe con trong.
     */
    List<TripViewModel> searchTrips(SearchTripForm form);

    /**
     * E2 - Lay 1 chuyen (dang AVAILABLE) de hien trang chon ghe.
     * Nem TripNotFoundException neu chuyen khong ton tai / khong con mo ban.
     */
    TripViewModel getTripForBooking(UUID tripId);

    /** E2 - Danh sach ma ghe DA co nguoi dat cua 1 chuyen (de khoa tren so do ghe). */
    List<String> getOccupiedSeatCodes(UUID tripId);

    /**
     * E2/E3 - GIU GHE TAM khi khach buoc vao trang thanh toan.
     * Ghe duoc giu trong SEAT_HOLD_MINUTES phut; nguoi khac khong chon duoc trong thoi gian nay.
     * Neu qua han ma chua thanh toan, scheduler se tu giai phong.
     * Nem SeatAlreadyBookedException neu ghe vua bi nguoi khac chiem.
     *
     * @return thoi diem het han giu ghe (de FE dem nguoc)
     */
    LocalDateTime holdSeats(UUID tripId, List<String> seatCodes, UUID accountId);

    /** Giai phong cac ghe giu tam da qua han. Scheduler goi dinh ky. Tra ve so ghe da don. */
    int releaseExpiredSeatHolds();

    /**
     * E3 - Tao booking: kiem tra ghe con trong, tao Booking + BookingDetail (moi ghe) +
     * chuyen ghe DANG GIU TAM sang DA DAT + Payment. Tra ve id cua booking vua tao.
     */
    UUID createBooking(CreateBookingForm form, UUID accountId);

    /** E4 - Danh sach tat ca ve cua khach hang (moi nhat truoc). */
    List<BookingViewModel> getMyBookings(UUID accountId);

    /**
     * E4 - Chi tiet 1 ve, co kiem tra quyen so huu.
     * Nem BookingNotFoundException neu ve khong ton tai hoac khong thuoc ve khach nay.
     */
    BookingViewModel getMyBooking(UUID bookingId, UUID accountId);

    /**
     * E5 - Xem TRUOC so tien duoc hoan neu huy ve bay gio (khong thay doi du lieu).
     * Dung de hien o man hinh xac nhan huy.
     */
    RefundViewModel previewRefund(UUID bookingId, UUID accountId);

    /**
     * E5 - Huy ve: tinh tien hoan theo chinh sach, ghi nhan giao dich hoan tien,
     * giai phong ghe va xoa du lieu dat ve.
     * Nem CannotCancelBookingException neu chuyen da khoi hanh.
     *
     * @return thong tin khoan hoan de hien cho khach
     */
    RefundViewModel cancelBooking(UUID bookingId, UUID accountId);

    /**
     * E6 - Danh gia chuyen di (chi sau khi chuyen hoan thanh, moi ve chi 1 lan).
     * Nem CannotReviewException neu chua hoan thanh hoac da danh gia truoc do.
     */
    void reviewBooking(UUID bookingId, UUID accountId, CreateReviewForm form);

    /** E6 - Kiem tra 1 ve da duoc danh gia hay chua (de an/hien form danh gia). */
    boolean hasReviewed(UUID bookingId);
}
