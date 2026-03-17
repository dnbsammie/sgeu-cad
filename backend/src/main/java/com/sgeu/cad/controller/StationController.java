package com.sgeu.cad.controller;

import com.sgeu.cad.dto.CreateStationRequest;
import com.sgeu.cad.model.Station;
import com.sgeu.cad.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stations")
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Station create(@Valid @RequestBody CreateStationRequest request) {
		return stationService.create(request);
	}
}

