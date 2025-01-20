package com.haven.app.haven.repository;

import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String>, JpaSpecificationExecutor<Transactions> {

    Page<Transactions> findAllByUser(Users user, Pageable pageable);

    List<Transactions> findAllByUser(Users user);

    List<Transactions> findAllByTracker(TrackerDevices tracker);
}
