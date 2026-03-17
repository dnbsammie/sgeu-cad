package com.sgeu.cad.controller;

import com.sgeu.cad.dto.CreateEmergencyRequest;
import com.sgeu.cad.dto.EmergencyResponse;
import com.sgeu.cad.service.EmergencyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {
	private final EmergencyService emergencyService;

	public EmergencyController(EmergencyService emergencyService) {
		this.emergencyService = emergencyService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmergencyResponse log(@Valid @org.springframework.web.bind.annotation.RequestBody CreateEmergencyRequest request,
								 @RequestHeader(name = "X-Actor", defaultValue = "system") String actor) {
		return emergencyService.logEmergency(request, actor);
	}

	@GetMapping("/{id}")
	public EmergencyResponse get(@PathVariable UUID id) {
		return emergencyService.getEmergency(id);
	}
}

