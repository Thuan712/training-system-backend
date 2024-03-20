package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionSpecification extends BaseSpecification<Section, SectionRequest> {
    @Override
    public Specification<Section> getFilter(SectionRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("courseId", request.getCourseId()))
                        .and(attributeContains("code", request.getCode()))
                        .and(attributeIdsNotIn(request.getExcludeIds()))
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

    private Specification<Section> attributeIdsNotIn(List<Long> excludeIds) {
        return ((root, query, criteriaBuilder) -> {
            if (excludeIds == null || excludeIds.size() < 1) {
                return null;
            }


            return criteriaBuilder.not(criteriaBuilder.in(root.get("id")).value(excludeIds));
        });
    }
}
