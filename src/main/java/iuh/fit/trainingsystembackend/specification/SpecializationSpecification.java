package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.request.SpecializationRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecializationSpecification extends BaseSpecification<Specialization, SpecializationRequest> {
    @Override
    public Specification<Specialization> getFilter(SpecializationRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeContains("name", request.getName()))
                        .and(attributeEqual("facultyId", request.getFacultyId()))
                        .and(attributeContains("code", request.getCode()))
                        .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Specialization> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Specialization> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
