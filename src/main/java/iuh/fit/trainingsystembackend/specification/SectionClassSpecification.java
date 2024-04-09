package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@Component
public class SectionClassSpecification extends BaseSpecification<SectionClass, SectionClassRequest> {
    @Override
    public Specification<SectionClass> getFilter(SectionClassRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("termId", request.getTermId()))
                        .and(attributeEqual("sectionId", request.getSectionId()))
                        .and(attributeStudentIdEqual(request.getStudentId()))
                        .and(attributeContains("code", request.getCode()))
                        .and(attributeEqual("sectionClassType", request.getSectionClassType()))
                        .and(attributeEqual("createStatus", request.getCreateStatus()))

                        // Select Section Class
                        .and(attributeEqual("id", request.getSectionClassId()).or(attributeEqual("refId", request.getSectionClassId())))
                        // Filter
                        .and(attributeContains("code", request.getSearchValue()))
                        .and(attributeSectionIdsIn( request.getSectionIds()))
                        .and(attributeLecturerIdsIn(request.getLecturerIds()))
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

    private Specification<SectionClass> attributeStudentIdEqual(Long studentId) {
        return ((root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return null;
            }

            Subquery<StudentSectionClass> subquery = query.subquery(StudentSectionClass.class);
            Root<StudentSectionClass> subqueryRoot = subquery.from(StudentSectionClass.class);

            Predicate studentPredicate = criteriaBuilder.in(subqueryRoot.get("studentId")).value(studentId);
            subquery.select(subqueryRoot.get("sectionClassId")).where(studentPredicate);

            return criteriaBuilder.in(root.get("id")).value(subquery);
        });
    }

    private Specification<SectionClass> attributeLecturerIdsIn(List<Long> lecturerIds) {
        return ((root, query, criteriaBuilder) -> {
            if (lecturerIds == null || lecturerIds.isEmpty()) {
                return null;
            }

            return criteriaBuilder.in(root.get("lecturerId")).value(lecturerIds);
        });
    }

    private Specification<SectionClass> attributeSectionIdsIn(List<Long> sectionIds) {
        return ((root, query, criteriaBuilder) -> {
            if (sectionIds == null || sectionIds.isEmpty()) {
                return null;
            }

            return criteriaBuilder.in(root.get("sectionId")).value(sectionIds);
        });
    }
}
