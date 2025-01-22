package com.haven.app.haven.repository;

import com.haven.app.haven.entity.TrackerDevices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerDevicesRepository extends JpaRepository<TrackerDevices, String>, JpaSpecificationExecutor<TrackerDevices> {
    TrackerDevices findBySerialNumber(String serialNumber);
}
