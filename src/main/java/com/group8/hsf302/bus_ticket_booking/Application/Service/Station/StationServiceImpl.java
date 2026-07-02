package com.group8.hsf302.bus_ticket_booking.Application.Service.Station;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StationViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.StationMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.StationRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepo stationRepo;
    private final StationMapper stationMapper;

    public StationServiceImpl(StationRepo stationRepo, StationMapper stationMapper) {
        this.stationRepo = stationRepo;
        this.stationMapper = stationMapper;
    }

    @Override
    public StationViewModel create(CreateStationForm form) {
        Station station = stationMapper.toEntity(form);
        stationRepo.save(station);
        return stationMapper.toViewModel(station);
    }

    @Override
    public List<StationViewModel> getAll() {
        // Lọc ra chỉ những trạm đang hoạt động (AVAILABLE)
        return stationRepo.findAll().stream()
                .filter(station -> station.getStatus() == Status.AVAILABLE)
                .map(stationMapper::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public StationViewModel getById(UUID id) {
        Station station = findActiveById(id);
        return stationMapper.toViewModel(station);
    }

    @Override
    public StationViewModel update(UUID id, UpdateStationForm form) {
        Station station = findActiveById(id);
        Station updatedStation = stationMapper.updateEntityFromForm(form, station);
        stationRepo.save(updatedStation);
        return stationMapper.toViewModel(updatedStation);
    }

    @Override
    public boolean delete(UUID id) {
        Station station = findActiveById(id);
        station.setStatus(Status.NOT_AVAILABLE); // Xóa mềm (Soft Delete)
        stationRepo.save(station);
        return true;
    }

    // Helper method: Tìm trạm đang hoạt động, nếu không thấy thì báo lỗi
    private Station findActiveById(UUID id) {
        return stationRepo.findById(id)
                .filter(station -> station.getStatus() == Status.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
    }
}