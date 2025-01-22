package com.haven.app.haven.specification;

import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriceSpecification {
    public static Specification<Prices> getSpecification(SearchRequestTransaction request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request != null) {
                if (StringUtils.hasText(request.getSearch())){
                    String search = request.getSearch().trim().toLowerCase();
                    List<Predicate> searchPredicates = new ArrayList<>();

                    searchPredicates.add(cb.like(cb.lower(root.get("priceType").as(String.class)),"%" + search + "%"));

                    try {
                        Double searchNumber = Double.parseDouble(search);
                        searchPredicates.add(cb.equal(root.get("price"), searchNumber));
                    } catch (NumberFormatException e) {
                        // Not a number, skip price search
                    }
                    predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
                }

                if (StringUtils.hasText(request.getStatus())){
                    List<String> typePrice = Arrays.asList(request.getStatus().split(","));

                    typePrice = typePrice.stream()
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();

                    typePrice.stream().forEach(s -> System.out.println(s));

                    predicates.add(cb.lower(root.get("priceType").as(String.class)).in(typePrice));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
