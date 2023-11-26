package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.request.StudentRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

@Component
public class StudentSpecification extends BaseSpecification<Student, StudentRequest> {
    @Override
    public Specification<Student> getFilter(StudentRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeEqual("specializationClassId", request.getSpecializationClassId()))
                        .and(attributeEqual("userId", request.getUserId()))
                        .and(attributeSectionClassIdEqual(request.getSectionClassId()))
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

    private Specification<Student> attributeSectionClassIdEqual(Long sectionClassId) {
        return ((root, query, criteriaBuilder) -> {
            if (sectionClassId == null) {
                return null;
            }

            Subquery<StudentSectionClass> subquery = query.subquery(StudentSectionClass.class);
            Root<StudentSectionClass> subqueryRoot = subquery.from(StudentSectionClass.class);

            Predicate studentPredicate = criteriaBuilder.in(subqueryRoot.get("sectionClassId")).value(sectionClassId);
            subquery.select(subqueryRoot.get("studentId")).where(studentPredicate);

            return criteriaBuilder.in(root.get("id")).value(subquery);
        });
    }

}
