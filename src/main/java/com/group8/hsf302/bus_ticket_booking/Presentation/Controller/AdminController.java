package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BusViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.Paging.PagedResponse;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Admin.AdminService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccessDeniedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Admin Console (Quan tri vien). Chi mo cho tai khoan role ADMIN.
 * Pham vi FE hoan thien theo phan BE da san sang: Bus (CRUD day du), Account (CRUD),
 * Route (xem danh sach), Trip (xem danh sach + xoa).
 * (Tao/sua Trip va quan ly Tram/Ghe chua lam do DTO BE con trong - de danh cho phan BE cua Quan.)
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Chi cho ADMIN vao. Giong pattern verifyDriverAuth cua DriverController. */
    private AccountViewModel verifyAdminAuth(HttpSession session) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            throw new AccessDeniedException("Vui long dang nhap de vao trang quan tri.");
        }
        if (currentUser.role() != Role.ADMIN) {
            throw new AccessDeniedException("Ban khong co quyen truy cap trang quan tri (can role ADMIN).");
        }
        return currentUser;
    }

    private void addCommon(Model model, AccountViewModel admin) {
        model.addAttribute("currentUser", admin);
    }

    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/admin/dashboard";
    }

    // ===== Dashboard: so lieu tong quan + chuyen dang hoat dong + feedback gan day =====
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "tripPage", defaultValue = "0") int tripPage,
                            HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        // The so lieu tong quan
        model.addAttribute("totalAccounts", adminService.getAllAccounts(0, 1).getTotalElements());
        model.addAttribute("totalBuses", adminService.getAllBuses(0, 1).getTotalElements());
        model.addAttribute("totalRoutes", adminService.getAllRoutes(0, 1).getTotalElements());
        model.addAttribute("totalTrips", adminService.getAllTrips(0, 1).getTotalElements());
        model.addAttribute("totalActiveTrips", adminService.countActiveTrips());
        model.addAttribute("totalPendingRefunds", adminService.countPendingRefunds());
        model.addAttribute("totalReviews", adminService.countReviews());
        // Chuyen dang hoat dong (phan trang) + 5 danh gia moi nhat
        model.addAttribute("activeTrips", adminService.getActiveTrips(tripPage, 5));
        model.addAttribute("recentReviews", adminService.getAllReviews(0, 5).getContent());
        return "admin/dashboard";
    }

    // ===================== DUYET HOAN TIEN =====================
    @GetMapping("/refunds")
    public String refunds(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("refunds", adminService.getPendingRefunds(page, 10));
        return "admin/refunds";
    }

    @PostMapping("/refunds/{id}/approve")
    public String approveRefund(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        boolean ok = adminService.approveRefund(id);
        ra.addFlashAttribute(ok ? "successMessage" : "errorMessage",
                ok ? "Đã duyệt hoàn tiền cho khách hàng." : "Không tìm thấy yêu cầu hoàn tiền.");
        return "redirect:/admin/refunds";
    }

    // ===================== FEEDBACK KHACH HANG =====================
    @GetMapping("/reviews")
    public String reviews(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("reviews", adminService.getAllReviews(page, 10));
        return "admin/reviews";
    }

    // ===================== BUS (CRUD day du) =====================
    @GetMapping("/buses")
    public String buses(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(required = false) String q,
                        HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        PagedResponse<BusViewModel> buses = (q != null && !q.isBlank())
                ? adminService.getBusByName(q, page, 10)
                : adminService.getAllBuses(page, 10);
        model.addAttribute("buses", buses);
        model.addAttribute("q", q);
        model.addAttribute("busTypes", BusType.values());
        model.addAttribute("busCapacities", BusCapacity.values());
        model.addAttribute("statuses", Status.values());
        return "admin/buses";
    }

    @PostMapping("/buses/create")
    public String createBus(@ModelAttribute AdminCreateBusForm form, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.createBus(form);
        ra.addFlashAttribute("successMessage", "Da them xe moi.");
        return "redirect:/admin/buses";
    }

    @PostMapping("/buses/{id}/update")
    public String updateBus(@PathVariable UUID id, @ModelAttribute AdminUpdateBusForm form,
                            RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.updateBus(form, id);
        ra.addFlashAttribute("successMessage", "Da cap nhat xe.");
        return "redirect:/admin/buses";
    }

    @PostMapping("/buses/{id}/delete")
    public String deleteBus(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.deleteBus(id);
        ra.addFlashAttribute("successMessage", "Da xoa xe.");
        return "redirect:/admin/buses";
    }

    // ===================== ACCOUNT (CRUD) =====================
    @GetMapping("/accounts")
    public String accounts(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String q,
                           HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        PagedResponse<AccountViewModel> accounts = (q != null && !q.isBlank())
                ? adminService.getAccountByName(q, page, 10)
                : adminService.getAllAccounts(page, 10);
        model.addAttribute("accounts", accounts);
        model.addAttribute("q", q);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", Status.values());
        return "admin/accounts";
    }

    @PostMapping("/accounts/create")
    public String createAccount(@ModelAttribute AdminCreateAccountForm form, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.createAccount(form);
        ra.addFlashAttribute("successMessage", "Da tao tai khoan.");
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/update")
    public String updateAccount(@PathVariable UUID id, @ModelAttribute AdminUpdateAccountForm form,
                                RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.updateAccount(form, id);
        ra.addFlashAttribute("successMessage", "Da cap nhat tai khoan.");
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/delete")
    public String deleteAccount(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.deleteAccount(id);
        ra.addFlashAttribute("successMessage", "Da xoa tai khoan.");
        return "redirect:/admin/accounts";
    }

    // ===================== ROUTE (B2 - CRUD day du) =====================
    @GetMapping("/routes")
    public String routes(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("routes", adminService.getAllRoutes(page, 10));
        // Danh sach tram cho dropdown chon diem dung khi tao/sua tuyen
        model.addAttribute("stations", adminService.getAllStations(0, 1000).getContent());
        return "admin/routes";
    }

    @PostMapping("/routes/create")
    public String createRoute(@ModelAttribute AdminCreateRouteForm form, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.createRoute(form);
        ra.addFlashAttribute("successMessage", "Da them tuyen moi.");
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/{id}/update")
    public String updateRoute(@PathVariable UUID id, @ModelAttribute AdminUpdateRouteForm form,
                              RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.updateRoute(form, id);
        ra.addFlashAttribute("successMessage", "Da cap nhat tuyen.");
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/{id}/delete")
    public String deleteRoute(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.deletedRoute(id);
        ra.addFlashAttribute("successMessage", "Da xoa tuyen.");
        return "redirect:/admin/routes";
    }

    // ===================== STATION (B2 - Quan ly ben/tram) =====================
    @GetMapping("/stations")
    public String stations(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String q,
                           HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("stations", (q != null && !q.isBlank())
                ? adminService.getStationByName(q, page, 10)
                : adminService.getAllStations(page, 10));
        model.addAttribute("q", q);
        model.addAttribute("statuses", Status.values());
        return "admin/stations";
    }

    @PostMapping("/stations/create")
    public String createStation(@ModelAttribute AdminCreateStationForm form, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.createStation(form);
        ra.addFlashAttribute("successMessage", "Da them tram.");
        return "redirect:/admin/stations";
    }

    @PostMapping("/stations/{id}/update")
    public String updateStation(@PathVariable UUID id, @ModelAttribute AdminUpdateStationForm form,
                                RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.updateStation(form, id);
        ra.addFlashAttribute("successMessage", "Da cap nhat tram.");
        return "redirect:/admin/stations";
    }

    @PostMapping("/stations/{id}/delete")
    public String deleteStation(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.deletedStation(id);
        ra.addFlashAttribute("successMessage", "Da xoa tram.");
        return "redirect:/admin/stations";
    }

    // ===================== TRIP (B4 - CRUD) =====================
    // Da mo lai sau khi hoan thien BE (getAllTrips/createTrip/updateTrip/deletedTrip).
    @GetMapping("/trips")
    public String trips(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("trips", adminService.getAllTrips(page, 10));
        // Danh sach tuyen + xe cho dropdown trong modal them/sua
        model.addAttribute("routes", adminService.getAllRoutes(0, 1000).getContent());
        model.addAttribute("buses", adminService.getAllBuses(0, 1000).getContent());
        model.addAttribute("tripStatuses", TripStatus.values());
        return "admin/trips";
    }

    @PostMapping("/trips/create")
    public String createTrip(@ModelAttribute AdminCreateTripForm form, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.createTrip(form);
        ra.addFlashAttribute("successMessage", "Da them chuyen moi.");
        return "redirect:/admin/trips";
    }

    @PostMapping("/trips/{id}/update")
    public String updateTrip(@PathVariable UUID id, @ModelAttribute AdminUpdateTripForm form,
                             RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.updateTrip(form, id);
        ra.addFlashAttribute("successMessage", "Da cap nhat chuyen.");
        return "redirect:/admin/trips";
    }

    @PostMapping("/trips/{id}/delete")
    public String deleteTrip(@PathVariable UUID id, RedirectAttributes ra, HttpSession session) {
        verifyAdminAuth(session);
        adminService.deletedTrip(id);
        ra.addFlashAttribute("successMessage", "Da xoa chuyen.");
        return "redirect:/admin/trips";
    }
}
