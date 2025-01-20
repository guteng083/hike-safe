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
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("role"), role));

            if (searchRequest != null) {
                if (StringUtils.hasText(searchRequest.getName())) {
                    Join<Users, UsersDetail> join = root.join("usersDetail", JoinType.LEFT);
                    String name = searchRequest.getName().trim().toLowerCase();
                    predicates.add(cb.like(cb.lower(join.get("fullName")), "%" + name + "%"));
                }

                if (StringUtils.hasText(searchRequest.getEmail())) {
                    String email = searchRequest.getEmail().trim();
                    predicates.add(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
