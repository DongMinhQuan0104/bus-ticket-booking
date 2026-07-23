package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBookingForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Controller cho luong dat ve / thanh toan (E3) - tang Presentation.
 * <ul>
 *   <li>GET /bookings/checkout: nhan ghe da chon tu E2, hien trang xac nhan + chon phuong thuc thanh toan</li>
 *   <li>POST /bookings/confirm: goi service tao booking that su roi chuyen sang trang "Ve cua toi" (E4)</li>
 * </ul>
 * Ca hai deu yeu cau dang nhap; neu chua dang nhap se chuyen ve trang login.
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final CustomerService customerService;

    public BookingController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * E3 - Trang xac nhan & thanh toan.
     * Nhan tham so tu E2 (tripId, danh sach ghe dang CSV, ten hanh khach, lien he),
     * ghep ghe voi ten hanh khach va tinh tong tien de hien tom tat truoc khi xac nhan.
     */
    @GetMapping("/checkout")
    public String checkout(@RequestParam UUID tripId,
                           @RequestParam(required = false) String seats,
                           @RequestParam(name = "passengerName", required = false) List<String> passengerNames,
                           @RequestParam(required = false) String contactPhone,
                           @RequestParam(required = false) String contactEmail,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để đặt vé");
            return "redirect:/auth/login";
        }

        List<String> seatCodes = parseSeats(seats);
        if (seatCodes.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn chưa chọn ghế nào");
            return "redirect:/trips/" + tripId + "/booking";
        }

        TripViewModel trip = customerService.getTripForBooking(tripId);

        // Ghep ma ghe voi ten hanh khach (thieu ten -> dung ten tai khoan)
        List<PassengerRow> passengers = new ArrayList<>();
        for (int i = 0; i < seatCodes.size(); i++) {
            String name = (passengerNames != null && i < passengerNames.size()
                    && passengerNames.get(i) != null && !passengerNames.get(i).isBlank())
                    ? passengerNames.get(i).trim() : currentUser.fullName();
            passengers.add(new PassengerRow(seatCodes.get(i), name));
        }

        double unit = trip.price() != null ? trip.price() : 0.0;

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("trip", trip);
        model.addAttribute("seatCodes", seatCodes);
        model.addAttribute("passengers", passengers);
        model.addAttribute("contactPhone", contactPhone != null ? contactPhone : currentUser.phoneNumber());
        model.addAttribute("contactEmail", contactEmail != null ? contactEmail : currentUser.email());
        model.addAttribute("unitPrice", unit);
        model.addAttribute("totalPrice", unit * seatCodes.size());
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "booking-checkout";
    }

    /**
     * E3 - Xac nhan dat ve: goi createBooking (tao Booking/BookingDetail/giu ghe/Payment).
     * Neu ghe vua bi nguoi khac dat, service nem SeatAlreadyBookedException -> GlobalExceptionHandler xu ly.
     */
    @PostMapping("/confirm")
    public String confirm(@Valid @ModelAttribute("bookingForm") CreateBookingForm form,
                          BindingResult bindingResult,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để đặt vé");
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đặt vé không hợp lệ");
            return "redirect:/trips/" + form.getTripId() + "/booking";
        }

        customerService.createBooking(form, currentUser.id());
        redirectAttributes.addFlashAttribute("successMessage", "Đặt vé thành công!");
        return "redirect:/my-tickets";
    }

    /** Tach chuoi ghe dang CSV "A01,A02" (nhan tu E2) thanh danh sach ["A01","A02"], bo phan tu rong. */
    private List<String> parseSeats(String seats) {
        List<String> result = new ArrayList<>();
        if (seats == null || seats.isBlank()) {
            return result;
        }
        for (String s : Arrays.asList(seats.split(","))) {
            if (s != null && !s.isBlank()) {
                result.add(s.trim());
            }
        }
        return result;
    }

    // Dong du lieu hien thi hanh khach tren trang checkout (ghep ma ghe voi ten hanh khach)
    public record PassengerRow(String seatCode, String passengerName) {}
}
