package com.haven.app.haven.specification;

import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
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
import java.util.Arrays;
import java.util.List;


public class TransactionSpecification {
    public static Specification<Transactions> getSpecification(SearchRequestTransaction searchRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest != null) {

                if (StringUtils.hasText(searchRequest.getSearch())) {
                    String search = searchRequest.getSearch().trim().toLowerCase();
                    List<Predicate> searchPredicates = new ArrayList<>();


                    Join<Transactions, Users> userJoin = root.join("user", JoinType.LEFT);
                    Join<Users, UsersDetail> userDetailJoin = userJoin.join("usersDetail", JoinType.LEFT);
                    Join<Transactions, TrackerDevices> trackerJoin = root.join("tracker", JoinType.LEFT);


                    searchPredicates.add(cb.like(cb.lower(userJoin.get("email")), "%" + search + "%"));
                    searchPredicates.add(cb.like(cb.lower(userDetailJoin.get("fullName")), "%" + search + "%"));
                    searchPredicates.add(cb.like(cb.lower(trackerJoin.get("serialNumber")), "%" + search + "%"));


                    predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
                }


                if (StringUtils.hasText(searchRequest.getStatus())) {

                    List<String> statusList = Arrays.asList(searchRequest.getStatus().split(","));

                    statusList = statusList.stream()
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();

                    statusList.stream().forEach(s -> System.out.println(s));

                    predicates.add(cb.lower(root.get("status").as(String.class)).in(statusList));
                }
            }


            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}