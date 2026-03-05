package com.condofacil.repository.spec;

import com.condofacil.dto.CondominiumFilterDTO;
import com.condofacil.model.Condominium;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CondominiumSpecs {

    public static Specification<Condominium> usingFilter(CondominiumFilterDTO filter){
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() !=null && !filter.name().isBlank()){
                predicates.add(builder.like(builder.lower(root.get("name")),
                        "%" + filter.name().toLowerCase() + "%"));
            }

            if (filter.taxId() != null && !filter.taxId().isBlank()){
                predicates.add(builder.equal(root.get("taxId"), filter.taxId()));
            }

            if (filter.city() != null && !filter.city().isBlank()){
                predicates.add(builder.equal(builder.lower(root.get("city")),
                        filter.city().toLowerCase()));
            }

            if (filter.state() != null && !filter.state().isBlank()){
                predicates.add(builder.equal(root.get("state"), filter.state()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
