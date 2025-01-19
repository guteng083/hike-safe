package com.haven.app.haven.repository;

import com.haven.app.haven.entity.TrackerDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerDevicesRepository extends JpaRepository<TrackerDevices, String> {
    TrackerDevices findBySerialNumber(String serialNumber);
}
