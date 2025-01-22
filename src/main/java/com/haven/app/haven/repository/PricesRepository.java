package com.haven.app.haven.repository;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.entity.Prices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricesRepository extends JpaRepository<Prices, String> {
    Prices findByPriceType(PriceType priceType);
}
