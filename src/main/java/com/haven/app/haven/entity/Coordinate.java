package com.haven.app.haven.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "coordinate")
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tracker_id", nullable = false)
    private TrackerDevices trackerId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transactionId;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @CreationTimestamp
//    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
