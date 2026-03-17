package com.sgeu.cad.service;

import com.sgeu.cad.dto.CreateStationRequest;
import com.sgeu.cad.exception.NotFoundException;
import com.sgeu.cad.model.Station;
import com.sgeu.cad.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.sgeu.cad.service.MappingSupport.toLocation;

@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional
	public Station create(CreateStationRequest request) {
		Station station = Station.builder()
			.name(request.name())
			.location(toLocation(request.location()))
			.build();
		return stationRepository.save(station);
	}

	@Transactional(readOnly = true)
	public Station get(UUID id) {
		return stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Station not found: " + id));
	}
}

