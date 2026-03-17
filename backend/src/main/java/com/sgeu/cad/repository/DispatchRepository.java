package com.sgeu.cad.repository;

import com.sgeu.cad.model.Dispatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DispatchRepository extends JpaRepository<Dispatch, UUID> {
}

