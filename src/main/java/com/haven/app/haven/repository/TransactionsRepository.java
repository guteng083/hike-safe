package com.haven.app.haven.repository;

import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {
    Transactions findByTracker(TrackerDevices trackerDevices);
    List<Transactions> findByUserId(String userId);
}
