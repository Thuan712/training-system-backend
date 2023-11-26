package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseSpecification extends BaseSpecification<Course, CourseRequest> {
    @Override
    public Specification<Course> getFilter(CourseRequest request) {
        return (root, query, criteriaBuilder) -> Specification.where(attributeContains("name", request.getName()))
                .and(attributeContains("code", request.getCode()))
                .and(attributeEqual("credit", request.getCredit()))
                .and(attributeEqual("deleted", request.getDeleted()))
                //TODO: Attribute course require in
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
}
