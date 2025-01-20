package com.haven.app.haven.specification;

import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transactions> getSpecification(SearchRequest searchRequest) {
        return (root, query, cb) -> {
            try {
                List<Predicate> predicates = new ArrayList<>();

                if (searchRequest != null && StringUtils.hasText(searchRequest.getSearch())) {
                    String search = searchRequest.getSearch().trim().toLowerCase();

                    List<Predicate> searchPredicates = new ArrayList<>();

                    // Join with Users and UsersDetail for searching user information
                    Join<Transactions, Users> userJoin = root.join("user", JoinType.LEFT);
                    Join<Users, UsersDetail> userDetailJoin = userJoin.join("usersDetail", JoinType.LEFT);

                    // Search by user's email
                    searchPredicates.add(cb.like(cb.lower(userJoin.get("email")), "%" + search + "%"));

                    // Search by user's full name
                    searchPredicates.add(cb.like(cb.lower(userDetailJoin.get("fullName")), "%" + search + "%"));

                    // Search by transaction status
                    searchPredicates.add(cb.like(cb.lower(root.get("status").as(String.class)), "%" + search + "%"));

                    // Search by tracker device (if needed)
                    Join<Transactions, TrackerDevices> trackerJoin = root.join("tracker", JoinType.LEFT);
                    searchPredicates.add(cb.like(cb.lower(trackerJoin.get("id")), "%" + search + "%"));

                    // Add the OR condition for all search predicates
                    predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
                }

                // Add default sorting by createdAt descending
                query.orderBy(cb.desc(root.get("createdAt")));

                return cb.and(predicates.toArray(new Predicate[0]));
            } catch (Exception e) {
                throw e;
            }
        };
    }
}
