package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SectionSpecification extends BaseSpecification<Section, SectionRequest> {
    @Override
    public Specification<Section> getFilter(SectionRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("courseId", request.getCourseId()))
                        .and(attributeContains("code", request.getCode()))
                        .and(attributeEqual("theoryPeriods", request.getTheoryPeriods()))
                        .and(attributeEqual("practicePeriods", request.getPracticePeriods()))
                        .and(attributeEqual("sectionType", request.getSectionType()))
                        .and(attributeEqual("deleted", request.getDeleted()))
                        .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Section> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Section> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

}
