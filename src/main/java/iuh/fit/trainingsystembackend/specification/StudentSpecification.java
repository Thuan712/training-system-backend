package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.request.StudentRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StudentSpecification extends BaseSpecification<Student, StudentRequest> {
    @Override
    public Specification<Student> getFilter(StudentRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeEqual("specializationClassId", request.getSpecializationClassId()))
                        .and(attributeEqual("userId", request.getUserId()))
                        .and(attributeEqual("academicYearId", request.getAcademicYearId()))
                        .and(attributeEqual("typeOfEducation", request.getTypeOfEducation()))
                        .and(attributeEqual("specializationId", request.getSpecializationId()))
                        .toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private Specification<Student> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Student> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

}
