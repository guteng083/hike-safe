package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.TrackerStatus;
import com.haven.app.haven.dto.request.SearchTrackerDeviceRequest;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.TrackerDeviceException;
import com.haven.app.haven.repository.TrackerDevicesRepository;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.specification.TrackerDeviceSpecification;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackerDevicesServiceImpl implements TrackerDevicesService {
    private final TrackerDevicesRepository trackerDevicesRepository;


    @Override
    public TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest) {
        try {
            TrackerDevices trackerDevices = TrackerDevices.builder()
                    .serialNumber(trackerDevicesRequest.getSerialNumber())
                    .build();

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            LogUtils.logSuccess("TrackerDevicesService", "createTracker");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.createTracker", e);
            throw new TrackerDeviceException("Failed to create tracker device");
        }
    }

    @Override
    public Page<TrackerDevicesResponse> getTrackerDevices(SearchTrackerDeviceRequest searchRequest) {
        try {
            Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize());

            Specification<TrackerDevices> specification = TrackerDeviceSpecification.getSpecification(searchRequest);


            Page<TrackerDevices> trackerDevices = trackerDevicesRepository.findAll(specification, pageable);

            LogUtils.logSuccess("TrackerDevicesService", "getTrackerDevices");

            return trackerDevices.map(TrackerDevicesResponse::trackerDevicesToTrackerDevicesResponse);
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.getTrackerDevices", e);
            if (e instanceof NotFoundException){
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device list");
        }
    }

    @Override
    public List<TrackerDevicesResponse> getTrackerDevicesWithoutPage(SearchTrackerDeviceRequest searchRequest) {
        try {
            Specification<TrackerDevices> specification = TrackerDeviceSpecification.getSpecification(searchRequest);

            List<TrackerDevices> trackerDevices = trackerDevicesRepository.findAll(specification);

            LogUtils.logSuccess("TrackerDevicesService", "getTrackerDevices");

            return trackerDevices.stream().map(TrackerDevicesResponse::trackerDevicesToTrackerDevicesResponse).toList();
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.getTrackerDevices", e);
            if (e instanceof NotFoundException){
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device list");
        }
    }

    @Override
    public TrackerDevicesResponse getTrackerById(String id) {
        try {
            TrackerDevices trackerDevices = getOne(id);

            LogUtils.logSuccess("TrackerDevicesService", "getTrackerById");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.getTrackerById", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device");
        }
    }

    @Override
    public TrackerDevices getOne(String id) {
        try {
            return trackerDevicesRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Tracker device not found"));
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.getOne", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device");
        }
    }

    @Override
    public TrackerDevicesResponse updateTracker(String id, TrackerDevicesRequest trackerDevicesRequest) {
        try {
            TrackerDevices trackerDevices = getOne(id);

            trackerDevices.setSerialNumber(trackerDevicesRequest.getSerialNumber());
            trackerDevicesRepository.saveAndFlush(trackerDevices);

            LogUtils.logSuccess("TrackerDevicesService", "updateTracker");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.updateTracker", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to update tracker device");
        }
    }

    @Override
    public TrackerDevicesResponse updateStatus(String id, TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        try {
            TrackerDevices trackerDevices = getOne(id);
            trackerDevices.setStatus(TrackerStatus.getStatus(trackerDevicesStatusRequest.getStatus()));

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            LogUtils.logSuccess("TrackerDevicesService", "updateStatus");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.updateStatus", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to update tracker device status");
        }
    }

    @Override
    public void deleteTracker(String id) {
        try {
            TrackerDevices trackerDevices = getOne(id);
            trackerDevicesRepository.delete(trackerDevices);

            LogUtils.logSuccess("TrackerDevicesService", "deleteTracker");
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.deleteTracker", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to delete tracker device");
        }
    }

    @Override
    public TrackerDevices getBySerialNumber(String serialNumber) {
        try {
            TrackerDevices trackerDevices = trackerDevicesRepository.findBySerialNumber(serialNumber);

            if (trackerDevices == null) {
                throw new NotFoundException("Tracker device not found");
            }

            LogUtils.logSuccess("TrackerDevicesService", "getBySerialNumber");

            return trackerDevices;
        } catch (Exception e) {
            LogUtils.getError("TrackerDevicesService.getBySerialNumber", e);
            throw new TrackerDeviceException("Failed to get tracker device by serial number");
        }
    }
}
