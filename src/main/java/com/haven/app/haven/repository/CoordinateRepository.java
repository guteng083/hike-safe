package com.haven.app.haven.repository;

import com.haven.app.haven.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoordinateRepository extends JpaRepository<Coordinate, UUID> {
}
