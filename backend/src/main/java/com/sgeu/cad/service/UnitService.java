package com.sgeu.cad.service;

import com.sgeu.cad.dto.CreateUnitRequest;
import com.sgeu.cad.dto.UnitResponse;
import com.sgeu.cad.exception.NotFoundException;
import com.sgeu.cad.model.Unit;
import com.sgeu.cad.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.sgeu.cad.service.MappingSupport.toLocationDto;

@Service
public class UnitService {
	private final UnitRepository unitRepository;
	private final StationService stationService;

	public UnitService(UnitRepository unitRepository, StationService stationService) {
		this.unitRepository = unitRepository;
		this.stationService = stationService;
	}

	@Transactional
	public UnitResponse create(CreateUnitRequest request) {
		Unit unit = Unit.builder()
			.agency(request.agency())
			.callsign(request.callsign())
			.homeStation(stationService.get(request.homeStationId()))
			.status(request.status())
			.activePersonnel(request.activePersonnel())
			.fuelPercent(request.fuelPercent())
			.lastKnownLocation(null)
			.availableFrom(null)
			.build();
		unit = unitRepository.save(unit);
		return toResponse(unit);
	}

	@Transactional(readOnly = true)
	public UnitResponse get(UUID id) {
		return unitRepository.findById(id)
			.map(this::toResponse)
			.orElseThrow(() -> new NotFoundException("Unit not found: " + id));
	}

	private UnitResponse toResponse(Unit unit) {
		return new UnitResponse(
			unit.getId(),
			unit.getAgency(),
			unit.getCallsign(),
			unit.getHomeStation().getId(),
			unit.getStatus(),
			unit.getActivePersonnel(),
			unit.getFuelPercent(),
			toLocationDto(unit.getLastKnownLocation()),
			unit.getAvailableFrom()
		);
	}
}

