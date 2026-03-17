package com.sgeu.cad.controller;

import com.sgeu.cad.dto.CreateUnitRequest;
import com.sgeu.cad.dto.UnitResponse;
import com.sgeu.cad.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/units")
public class UnitController {
	private final UnitService unitService;

	public UnitController(UnitService unitService) {
		this.unitService = unitService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UnitResponse create(@Valid @RequestBody CreateUnitRequest request) {
		return unitService.create(request);
	}

	@GetMapping("/{id}")
	public UnitResponse get(@PathVariable UUID id) {
		return unitService.get(id);
	}
}

