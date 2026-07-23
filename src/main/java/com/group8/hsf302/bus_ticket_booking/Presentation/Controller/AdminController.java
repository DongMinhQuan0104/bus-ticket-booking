package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateBusForm;
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

    // ===== Dashboard: vai so lieu tong quan =====
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("totalAccounts", adminService.getAllAccounts(0, 1).getTotalElements());
        model.addAttribute("totalBuses", adminService.getAllBuses(0, 1).getTotalElements());
        model.addAttribute("totalRoutes", adminService.getAllRoutes(0, 1).getTotalElements());
        return "admin/dashboard";
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

    // ===================== ROUTE (xem danh sach) =====================
    @GetMapping("/routes")
    public String routes(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        AccountViewModel admin = verifyAdminAuth(session);
        addCommon(model, admin);
        model.addAttribute("routes", adminService.getAllRoutes(page, 10));
        return "admin/routes";
    }

    // ===================== TRIP =====================
    // Da BO khoi Admin Console: AdminService.getAllTrips() hien tra ve null (BE Quan chua cai dat).
    // Se mo lai khi phan BE quan ly Trip (getAllTrips/createTrip/updateTrip) duoc hoan thien.
}
