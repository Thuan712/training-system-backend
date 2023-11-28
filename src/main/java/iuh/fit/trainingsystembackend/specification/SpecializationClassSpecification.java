package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.SpecializationClass;
import iuh.fit.trainingsystembackend.request.SpecializationClassRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecializationClassSpecification extends BaseSpecification<SpecializationClass, SpecializationClassRequest> {
    @Override
    public Specification<SpecializationClass> getFilter(SpecializationClassRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeContains("name", request.getName()))
                        .and(attributeEqual("specializationId", request.getSpecializationId()))
                        .and(attributeContains("schoolYear", request.getSchoolYear()))
                        .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<SpecializationClass> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<SpecializationClass> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
