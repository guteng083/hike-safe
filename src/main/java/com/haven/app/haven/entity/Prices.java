package com.haven.app.haven.entity;

import com.haven.app.haven.constant.PriceType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Prices {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "price_type")
    private PriceType priceType;

    @Column(nullable = false)
    private Double price;
}
