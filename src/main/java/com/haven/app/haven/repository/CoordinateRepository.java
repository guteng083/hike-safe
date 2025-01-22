package com.haven.app.haven.repository;

import com.haven.app.haven.entity.Coordinates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinates, String> {
    Page<Coordinates> findAllByTransaction_Id(Pageable pageable, String transactionId);
}
