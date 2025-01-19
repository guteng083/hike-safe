package com.haven.app.haven.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "tracker_id", nullable = false)
    private TrackerDevices tracker;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transaction;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
