package com.haven.app.haven.repository;

import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {
    Transactions findByTracker(TrackerDevices trackerDevices);
}
