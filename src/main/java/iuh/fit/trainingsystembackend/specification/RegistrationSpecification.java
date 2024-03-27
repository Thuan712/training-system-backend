package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.request.RegistrationRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@Component
public class RegistrationSpecification  extends BaseSpecification<StudentSectionClass, RegistrationRequest> {
    @Override
    public Specification<StudentSectionClass> getFilter(RegistrationRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("studentId",request.getStudentId())
                        .and(attributeEqual("sectionClassId", request.getSectionClassId()))

                        .and(attributeTermIdIn(request.getTermIds()))
                        .and(attributeLecturerIdIn(request.getLecturerIds()))
                        .and(attributeSectionIdIn(request.getSectionIds()))
                        .and(attributeContainsSectionClass(request.getSearchValue()))
                ).toPredicate(root, query, criteriaBuilder);
    }

    private Specification<StudentSectionClass> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<StudentSectionClass> attributeContainsSectionClass(String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClasssPredicate = criteriaBuilder.like(criteriaBuilder.lower(subqueryRoot.get("code")), containsLowerCase(value));
            subquery.select(subqueryRoot.get("id")).where(sectionClasssPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }

    private Specification<StudentSectionClass> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

    private Specification<StudentSectionClass> attributeTermIdIn(List<Long> termIds) {
        return((root, query, criteriaBuilder) -> {
            if (termIds == null || termIds.isEmpty()) {
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClassPredicate = criteriaBuilder.in(subqueryRoot.get("termId")).value(termIds);
            subquery.select(subqueryRoot.get("id")).where(sectionClassPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }

    private Specification<StudentSectionClass> attributeLecturerIdIn(List<Long> lecturerIds) {
        return((root, query, criteriaBuilder) -> {
            if (lecturerIds == null || lecturerIds.isEmpty()) {
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClasssPredicate = criteriaBuilder.in(subqueryRoot.get("lecturerId")).value(lecturerIds);
            subquery.select(subqueryRoot.get("id")).where(sectionClasssPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }

    private Specification<StudentSectionClass> attributeSectionIdIn(List<Long> sectionIds) {
        return((root, query, criteriaBuilder) -> {
            if (sectionIds == null || sectionIds.isEmpty()) {
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClasssPredicate = criteriaBuilder.in(subqueryRoot.get("sectionId")).value(sectionIds);
            subquery.select(subqueryRoot.get("id")).where(sectionClasssPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }
}
