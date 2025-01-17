package com.haven.app.haven.entity;

import com.haven.app.haven.constant.TrackerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "tracker_devices")
public class TrackerDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, name = "serial_number")
    private String SerialNumber;

    @Enumerated(EnumType.STRING)
    private TrackerStatus status;

    @PrePersist
    public void prePersist() {
        this.status = TrackerStatus.NOT_USED;
    }
}
