package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseSpecification extends BaseSpecification<Course, CourseRequest> {
    @Override
    public Specification<Course> getFilter(CourseRequest request) {
        return (root, query, criteriaBuilder) -> Specification
                .where(attributeEqual("specializationId", request.getSpecializationId()))
                .and(attributeContains("name", request.getName()))
                .and(attributeContains("code", request.getCode()))
                .and(attributeEqual("courseType", request.getCourseType()))
                .and(attributeEqual("credits", request.getCredits()))
                .and(attributeEqual("costCredits", request.getCostCredits()))
                .and(attributeEqual("typeOfKnowledge", request.getTypeOfKnowledge()))

                .and(attributeEqual("deleted", request.getDeleted()))
                .and(attributeSpecializationIn(request.getSpecializationIds()))
                .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Course> attributeContains(String key, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Course> attributeEqual(String key, Object value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

    private Specification<Course> attributeTermRegisterIn(List<TermType> termTypes) {
        return ((root, query, criteriaBuilder) -> {
            if (termTypes == null || termTypes.isEmpty()) {
                return null;
            }

            return criteriaBuilder.in(root.get("termType")).value(termTypes);
        });
    }

    private Specification<Course> attributeSpecializationIn(List<Long> SpecializationIds) {
        return ((root, query, criteriaBuilder) -> {
            if (SpecializationIds == null || SpecializationIds.isEmpty()) {
                return null;
            }

            return criteriaBuilder.or(criteriaBuilder.in(root.get("specializationId")).value(SpecializationIds), criteriaBuilder.isNull(root.get("specializationId"))) ;
        });
    }
}
