package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller cho khach hang tuong tac voi chuyen xe (tang Presentation).
 * <ul>
 *   <li>E1: GET /trips/search - form tim chuyen + hien ket qua</li>
 *   <li>E2: GET /trips/{tripId}/booking - trang chon ghe cua 1 chuyen</li>
 * </ul>
 */
@Controller
@RequestMapping("/trips")
public class CustomerTripController {

    private final CustomerService customerService;

    public CustomerTripController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * E1 - Trang tim kiem chuyen xe.
     * Lan dau vao (khong co tham so submitted) chi hien form, chua validate.
     * Khi bam Tim: validate form (NotBlank, ngay khong qua khu) roi goi service tim chuyen.
     * SameStationException duoc bat tai day de gan loi inline vao o "diem den".
     */
    @GetMapping("/search")
    public String search(@Valid @ModelAttribute("searchForm") SearchTripForm searchForm,
                         BindingResult bindingResult,
                         @RequestParam(value = "submitted", required = false) String submitted,
                         HttpSession session,
                         Model model) {

        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("departureCities", customerService.getDepartureCities());
        model.addAttribute("arrivalCities", customerService.getArrivalCities());

        boolean isSubmitted = submitted != null;
        model.addAttribute("submitted", isSubmitted);

        // Lan dau vao trang (chua bam tim): chi hien form, khong validate
        if (!isSubmitted) {
            return "customer/trip-search";
        }

        model.addAttribute("showResults", false);
        if (!bindingResult.hasErrors()) {
            try {
                List<TripViewModel> trips = customerService.searchTrips(searchForm);
                model.addAttribute("trips", trips);
                model.addAttribute("showResults", true);
            } catch (SameStationException e) {
                bindingResult.rejectValue("destinationTo", "sameStation", e.getMessage());
            }
        }

        return "customer/trip-search";
    }

    /**
     * E2 - Trang chon ghe cua 1 chuyen.
     * Controller sinh san danh sach ma ghe theo suc chua xe (A01..An) va danh dau ghe da co nguoi dat,
     * de template chi viec ve so do ghe; viec chon ghe/nhap hanh khach xu ly o phia client roi POST sang E3.
     */
    @GetMapping("/{tripId}/booking")
    public String bookingPage(@PathVariable UUID tripId, HttpSession session, Model model,
                              RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");

        // Khach VANG LAI duoc xem/tim chuyen tu do, nhung den buoc chon ghe - dat ve thi phai dang nhap.
        // Ghi nho URL dang muon vao de dang nhap xong quay lai dung cho.
        if (currentUser == null) {
            session.setAttribute("REDIRECT_AFTER_LOGIN", "/trips/" + tripId + "/booking");
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để chọn ghế và đặt vé.");
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", currentUser);

        TripViewModel trip = customerService.getTripForBooking(tripId);
        List<String> occupiedSeatCodes = customerService.getOccupiedSeatCodes(tripId);

        // Sinh danh sach ma ghe theo suc chua thuc te cua xe (16 hoac 32 cho): A01, A02, ...
        int seats = trip.busCapacity() != null ? trip.busCapacity().getSeats() : 0;
        List<String> seatCodes = new ArrayList<>();
        for (int i = 1; i <= seats; i++) {
            seatCodes.add(String.format("A%02d", i));
        }

        model.addAttribute("trip", trip);
        model.addAttribute("seatCodes", seatCodes);
        model.addAttribute("occupiedSeatCodes", occupiedSeatCodes);
        model.addAttribute("maxSeatsPerBooking", 4);
        return "customer/trip-booking";
    }
}
