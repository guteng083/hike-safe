package com.haven.app.haven.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transactions transactionId;

    @Column(name = "hiker_name", nullable = false)
    private String hikerName;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;
}
