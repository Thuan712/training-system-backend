package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SectionClassSpecification extends BaseSpecification<SectionClass, SectionClassRequest> {
    @Override
    public Specification<SectionClass> getFilter(SectionClassRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("lecturerId", request.getLecturerId()))
                        .and(attributeEqual("sectionId", request.getSectionId()))
                        .and(attributeContains("classCode", request.getClassCode()))
                        .and(attributeContains("room", request.getRoom()))
                        .and(attributeEqual("periodFrom", request.getPeriodFrom()))
                        .and(attributeEqual("periodTo", request.getPeriodTo()))
                        .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<SectionClass> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<SectionClass> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
