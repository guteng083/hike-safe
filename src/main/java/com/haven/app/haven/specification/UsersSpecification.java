package com.haven.app.haven.specification;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UsersSpecification {
    public static Specification<Users> getSpecification(SearchRequest searchRequest, Role role) {
        return (root, query, cb) -> {
            try {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(root.get("role"), role));

                if (searchRequest != null && StringUtils.hasText(searchRequest.getSearch())) {
                    Join<Users, UsersDetail> join = root.join("usersDetail", JoinType.LEFT);
                    String search = searchRequest.getSearch().trim().toLowerCase();

                    List<Predicate> searchPredicates = new ArrayList<>();

                    searchPredicates.add(cb.like(cb.lower(join.get("fullName")), "%" + search + "%"));

                    searchPredicates.add(cb.like(cb.lower(root.get("email")), "%" + search + "%"));

                    predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            } catch (Exception e) {
                throw e;
            }
        };
    }
}
