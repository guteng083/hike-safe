package com.haven.app.haven.specification;

import com.haven.app.haven.dto.request.SearchTrackerDeviceRequest;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TrackerDeviceSpecification {
    public static Specification<TrackerDevices> getSpecification(SearchTrackerDeviceRequest searchRequest) {
        return (root, query, cb) -> {
            Join<TrackerDevices, Transactions> transactionsJoin = root.join("transactions", jakarta.persistence.criteria.JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (searchRequest != null) {
                if(StringUtils.hasText(searchRequest.getSearch())){
                    String search = searchRequest.getSearch().trim().toLowerCase();

                    predicates.add(cb.like(cb.lower(root.get("serialNumber")), "%" + search + "%"));
                }
                if(StringUtils.hasText(searchRequest.getStatus())){
                    List<String> statusList = List.of(searchRequest.getStatus().split(","));
                    statusList = statusList.stream()
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();
                    predicates.add(cb.lower(root.get("status").as(String.class)).in(statusList));
                }
            }

            query.orderBy(cb.desc(transactionsJoin.get("updatedAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
