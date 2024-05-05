package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.StudentTuition;
import iuh.fit.trainingsystembackend.request.StudentTuitionRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StudentTuitionSpecification extends BaseSpecification<StudentTuition, StudentTuitionRequest> {
    @Override
    public Specification<StudentTuition> getFilter(StudentTuitionRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeEqual("studentId", request.getStudentId()))
                        .and(attributeEqual("tuitionId", request.getTuitionId()))
                        .and(attributeEqual("status", request.getStatus()))
                        .toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private Specification<StudentTuition> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<StudentTuition> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
