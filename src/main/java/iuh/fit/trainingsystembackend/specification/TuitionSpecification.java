package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Tuition;
import iuh.fit.trainingsystembackend.request.TuitionRequest;
import iuh.fit.trainingsystembackend.request.UserRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TuitionSpecification extends BaseSpecification<Tuition, TuitionRequest> {
    @Override
    public Specification<Tuition> getFilter(TuitionRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeEqual("initialFee", request.getInitialFee()))
                        .toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private Specification<Tuition> attributeContains(String key, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Tuition> attributeEqual(String key, Object value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
