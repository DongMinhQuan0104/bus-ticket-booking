package com.group8.hsf302.bus_ticket_booking.Application.Service.Staff;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffCancelBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffChangeSeatForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffCheckInForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffConfirmPaymentForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffCreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffRefundForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffSupportRequestForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.StaffTripSearchForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StaffBookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StaffCheckInViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StaffSeatViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StaffSupportRequestViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StaffTripViewModel;

import java.util.List;
import java.util.UUID;

public interface StaffService {

    /**
     * Tìm các chuyến có thể bán vé theo điểm đi,
     * điểm đến và ngày khởi hành.
     */
    List<StaffTripViewModel> searchTrips(
            StaffTripSearchForm form
    );

    /**
     * Xem trạng thái ghế của chuyến trên một chặng cụ thể.
     */
    List<StaffSeatViewModel> getSeats(
            UUID tripId,
            Integer pickupOrder,
            Integer dropoffOrder
    );

    /**
     * Staff tạo Booking tại quầy và giữ ghế tạm thời.
     */
    StaffBookingViewModel createCounterBooking(
            StaffCreateBookingForm form,
            UUID staffId
    );

    /**
     * Xác nhận thanh toán và phát hành vé.
     */
    StaffBookingViewModel confirmPayment(
            UUID bookingId,
            StaffConfirmPaymentForm form,
            UUID staffId
    );

    /**
     * Tra cứu Booking theo mã Booking,
     * tên khách hoặc số điện thoại.
     */
    List<StaffBookingViewModel> searchBookings(
            String keyword
    );

    /**
     * Xem thông tin chi tiết một Booking.
     */
    StaffBookingViewModel getBooking(
            UUID bookingId
    );

    /**
     * Đổi ghế cho một hành khách trong Booking.
     */
    StaffBookingViewModel changeSeat(
            UUID bookingId,
            StaffChangeSeatForm form,
            UUID staffId
    );

    /**
     * Hủy Booking.
     */
    StaffBookingViewModel cancelBooking(
            UUID bookingId,
            StaffCancelBookingForm form,
            UUID staffId
    );

    /**
     * Xử lý hoặc chuyển yêu cầu hoàn tiền cho Admin.
     */
    StaffBookingViewModel processRefund(
            UUID bookingId,
            StaffRefundForm form,
            UUID staffId
    );

    /**
     * Check-in hành khách bằng mã vé.
     */
    StaffCheckInViewModel checkIn(
            StaffCheckInForm form,
            UUID staffId
    );

    /**
     * Tạo yêu cầu hỗ trợ hoặc khiếu nại.
     */
    StaffSupportRequestViewModel createSupportRequest(
            StaffSupportRequestForm form,
            UUID staffId
    );

    /**
     * Giải phóng các ghế đang giữ nhưng đã hết hạn.
     *
     * @return số bản ghi ghế đã được giải phóng
     */
    int releaseExpiredHolds();
}