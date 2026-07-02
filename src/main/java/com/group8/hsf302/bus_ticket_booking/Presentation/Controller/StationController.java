package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Station.StationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // Hiển thị danh sách tất cả trạm
    @GetMapping
    public String listStations(Model model) {
        model.addAttribute("stations", stationService.getAll());
        return "stations/list";
    }

    // Hiển thị form tạo trạm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createStationForm", new CreateStationForm());
        return "stations/create";
    }

    // Xử lý tạo trạm mới
    @PostMapping("/create")
    public String createStation(@Valid @ModelAttribute("createStationForm") CreateStationForm form,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "stations/create";
        }
        stationService.create(form);
        redirectAttributes.addFlashAttribute("successMessage", "Station created successfully");
        return "redirect:/stations";
    }

    // Hiển thị form sửa trạm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("updateStationForm", stationService.getById(id));
        return "stations/edit";
    }

    // Xử lý cập nhật trạm
    @PostMapping("/edit/{id}")
    public String updateStation(@PathVariable UUID id,
                                @Valid @ModelAttribute("updateStationForm") UpdateStationForm form,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "stations/edit";
        }
        stationService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Station updated successfully");
        return "redirect:/stations";
    }

    // Xử lý xóa trạm (Soft Delete)
    @PostMapping("/delete/{id}")
    public String deleteStation(@PathVariable UUID id,
                                RedirectAttributes redirectAttributes) {
        stationService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Station deleted successfully");
        return "redirect:/stations";
    }
}