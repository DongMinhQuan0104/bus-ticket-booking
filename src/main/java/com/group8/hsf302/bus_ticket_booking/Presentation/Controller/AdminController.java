package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.Paging.PagedResponse;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Admin.AdminService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stations")
    public String listStations(@RequestParam(required = false) String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        PagedResponse<StationViewModel> pageData;
        if (name != null && !name.trim().isEmpty()) {
            pageData = adminService.getStationByName(name, page, size);
            model.addAttribute("searchName", name);
        } else {
            pageData = adminService.getAllStations(page, size);
        }
        model.addAttribute("pageData", pageData);
        return "/station/list";
    }

    @GetMapping("/stations/create")
    public String showCreateStationForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AdminCreateStationForm());
        }
        return "/station/create";
    }

    @PostMapping("/stations/create")
    public String createStation(@Valid @ModelAttribute("form") AdminCreateStationForm form,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/station/create";
        }
        adminService.createStation(form);
        redirectAttributes.addFlashAttribute("successMessage", "Add new station successfully");
        return "redirect:/admin/stations";
    }

    @GetMapping("/stations/{id}/edit")
    public String showEditStationForm(@PathVariable UUID id, Model model) {
        if (!model.containsAttribute("form")) {
            StationViewModel station = adminService.getStationById(id);
            model.addAttribute("form", station);
        }
        return "/station/edit";
    }

    @PostMapping("/stations/{id}/edit")
    public String updateStation(@PathVariable UUID id,
                                @Valid @ModelAttribute("form") AdminUpdateStationForm form,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/station/edit";
        }
        adminService.updateStation(form, id);
        redirectAttributes.addFlashAttribute("successMessage", "Update station successfully");
        return "redirect:/admin/stations";
    }

    @PostMapping("/stations/{id}/delete")
    public String deleteStation(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        adminService.deletedStation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Delete station successfully");
        return "redirect:/admin/stations";
    }


    @GetMapping("/buses")
    public String listBuses(@RequestParam(required = false) String name,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        PagedResponse<BusViewModel> pageData;
        if (name != null && !name.trim().isEmpty()) {
            pageData = adminService.getBusByName(name, page, size);
            model.addAttribute("searchName", name);
        } else {
            pageData = adminService.getAllBuses(page, size);
        }
        model.addAttribute("pageData", pageData);
        return "admin/bus/list";
    }

    @GetMapping("/buses/create")
    public String showCreateBusForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AdminCreateBusForm());
        }
        return "admin/bus/create";
    }

    @PostMapping("/buses/create")
    public String createBus(@Valid @ModelAttribute("form") AdminCreateBusForm form,
                            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/bus/create";
        }
        adminService.createBus(form);
        redirectAttributes.addFlashAttribute("successMessage", "Create bus successfully");
        return "redirect:/admin/buses";
    }

    @GetMapping("/buses/{id}/edit")
    public String showEditBusForm(@PathVariable UUID id, Model model) {
        if (!model.containsAttribute("form")) {
            BusViewModel bus = adminService.getBusById(id);
            model.addAttribute("form", bus);
        }
        return "admin/bus/edit";
    }

    @PostMapping("/buses/{id}/edit")
    public String updateBus(@PathVariable UUID id,
                            @Valid @ModelAttribute("form") AdminUpdateBusForm form,
                            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/bus/edit";
        }
        adminService.updateBus(form, id);
        redirectAttributes.addFlashAttribute("successMessage", "Update bus successfully");
        return "redirect:/admin/buses";
    }

    @PostMapping("/buses/{id}/delete")
    public String deleteBus(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        adminService.deleteBus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Delete bus successfully!");
        return "redirect:/admin/buses";
    }


    // =========================================================================
    // 3. QUẢN LÝ TUYẾN ĐƯỜNG (ROUTE)
    // =========================================================================

    @GetMapping("/routes")
    public String listRoutes(@RequestParam(required = false) String name,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        PagedResponse<RouteViewModel> pageData;
        if (name != null && !name.trim().isEmpty()) {
            pageData = adminService.getRouteByName(name, page, size);
            model.addAttribute("searchName", name);
        } else {
            pageData = adminService.getAllRoutes(page, size);
        }
        model.addAttribute("pageData", pageData);
        return "admin/route/list";
    }

    @GetMapping("/routes/create")
    public String showCreateRouteForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AdminCreateRouteForm());
        }
        // Bơm danh sách các bến xe ra Dropdown để Admin chọn lúc tạo tuyến
        model.addAttribute("allStations", adminService.getAllStations(0, 1000));
        return "admin/route/create";
    }

    @PostMapping("/routes/create")
    public String createRoute(@Valid @ModelAttribute("form") AdminCreateRouteForm form,
                              BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("allStations", adminService.getAllStations(0, 1000));
            return "admin/route/create";
        }
        adminService.createRoute(form);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo tuyến đường thành công!");
        return "redirect:/admin/routes";
    }

    @GetMapping("/routes/{id}/edit")
    public String showEditRouteForm(@PathVariable UUID id, Model model) {
        if (!model.containsAttribute("form")) {
            RouteViewModel route = adminService.getRouteById(id);
            model.addAttribute("form", route);
        }
        model.addAttribute("allStations", adminService.getAllStations(0, 1000));
        return "admin/route/edit";
    }

    @PostMapping("/routes/{id}/edit")
    public String updateRoute(@PathVariable UUID id,
                              @Valid @ModelAttribute("form") AdminUpdateRouteForm form,
                              BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("allStations", adminService.getAllStations(0, 1000));
            return "admin/route/edit";
        }
        adminService.updateRoute(form, id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tuyến đường thành công!");
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/{id}/delete")
    public String deleteRoute(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        adminService.deletedRoute(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa tuyến đường!");
        return "redirect:/admin/routes";
    }


    // =========================================================================
    // 4. QUẢN LÝ TÀI KHOẢN (ACCOUNT)
    // =========================================================================

    @GetMapping("/accounts")
    public String listAccounts(@RequestParam(required = false) String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        PagedResponse<AccountViewModel> pageData;
        if (name != null && !name.trim().isEmpty()) {
            pageData = adminService.getAccountByName(name, page, size);
            model.addAttribute("searchName", name);
        } else {
            pageData = adminService.getAllAccounts(page, size);
        }
        model.addAttribute("pageData", pageData);
        return "admin/account/list";
    }

    @GetMapping("/accounts/create")
    public String showCreateAccountForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AdminCreateAccountForm());
        }
        return "admin/account/create";
    }

    @PostMapping("/accounts/create")
    public String createAccount(@Valid @ModelAttribute("form") AdminCreateAccountForm form,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/account/create";
        }
        adminService.createAccount(form);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo tài khoản thành công!");
        return "redirect:/admin/accounts";
    }

    @GetMapping("/accounts/{id}/edit")
    public String showEditAccountForm(@PathVariable UUID id, Model model) {
        if (!model.containsAttribute("form")) {
            AccountViewModel account = adminService.getAccountById(id);
            model.addAttribute("form", account);
        }
        return "admin/account/edit";
    }

    @PostMapping("/accounts/{id}/edit")
    public String updateAccount(@PathVariable UUID id,
                                @Valid @ModelAttribute("form") AdminUpdateAccountForm form,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/account/edit";
        }
        adminService.updateAccount(form, id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tài khoản thành công!");
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/delete")
    public String deleteAccount(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        adminService.deleteAccount(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã khóa/xóa tài khoản!");
        return "redirect:/admin/accounts";
    }


    // =========================================================================
    // 5. QUẢN LÝ CHUYẾN ĐI (TRIP)
    // =========================================================================

    @GetMapping("/trips")
    public String listTrips(@RequestParam(required = false) String routeName,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        PagedResponse<TripViewModel> pageData;
        if (routeName != null && !routeName.trim().isEmpty()) {
            pageData = adminService.getTripByRouteName(routeName, page, size);
            model.addAttribute("searchName", routeName);
        } else {
            pageData = adminService.getAllTrips(page, size);
        }
        model.addAttribute("pageData", pageData);
        return "admin/trip/list";
    }

    @GetMapping("/trips/create")
    public String showCreateTripForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AdminCreateTripForm());
        }

        model.addAttribute("allRoutes", adminService.getAllRoutes(0, 1000));
        model.addAttribute("allBuses", adminService.getAllBuses(0, 1000));

        model.addAttribute("allDrivers", adminService.getAccountByRole(Role.DRIVER));

        return "admin/trip/create";
    }

    @PostMapping("/trips/create")
    public String createTrip(@Valid @ModelAttribute("form") AdminCreateTripForm form,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Lỗi form thì phải bơm lại danh sách dropdown trước khi trả về trang cũ
            model.addAttribute("allRoutes", adminService.getAllRoutes(0, 1000));
            model.addAttribute("allBuses", adminService.getAllBuses(0, 1000));
            model.addAttribute("allDrivers", adminService.getAccountByRole(Role.DRIVER));
            return "admin/trip/create";
        }
        adminService.createTrip(form);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lên lịch chuyến đi thành công!");
        return "redirect:/admin/trips";
    }

    @GetMapping("/trips/{id}/edit")
    public String showEditTripForm(@PathVariable UUID id, Model model) {
        if (!model.containsAttribute("form")) {
            TripViewModel trip = adminService.getTripById(id);
            model.addAttribute("form", trip);
        }
        model.addAttribute("allRoutes", adminService.getAllRoutes(0, 1000));
        model.addAttribute("allBuses", adminService.getAllBuses(0, 1000));
        model.addAttribute("allDrivers", adminService.getAccountByRole(Role.DRIVER));
        return "admin/trip/edit";
    }

    @PostMapping("/trips/{id}/edit")
    public String updateTrip(@PathVariable UUID id,
                             @Valid @ModelAttribute("form") AdminUpdateTripForm form,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("allRoutes", adminService.getAllRoutes(0, 1000));
            model.addAttribute("allBuses", adminService.getAllBuses(0, 1000));
            model.addAttribute("allDrivers", adminService.getAccountByRole(Role.DRIVER));
            return "admin/trip/edit";
        }
        adminService.updateTrip(form, id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin chuyến đi thành công!");
        return "redirect:/admin/trips";
    }

    @PostMapping("/trips/{id}/delete")
    public String deleteTrip(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        adminService.deletedTrip(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã hủy chuyến đi thành công!");
        return "redirect:/admin/trips";
    }
}
