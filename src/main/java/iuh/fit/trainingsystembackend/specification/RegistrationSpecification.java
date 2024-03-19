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

@Component
public class RegistrationSpecification  extends BaseSpecification<StudentSectionClass, RegistrationRequest> {
    @Override
    public Specification<StudentSectionClass> getFilter(RegistrationRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("studentId",request.getStudentId())
                        .and(attributeEqual("sectionClassId", request.getSectionClassId()))
                        .and(attributeTermEqual(request.getTermId()))
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

    private Specification<StudentSectionClass> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

    private Specification<StudentSectionClass> attributeTermEqual(Long termId) {
        return((root, query, criteriaBuilder) -> {
            if (termId == null) {
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClasssPredicate = criteriaBuilder.in(subqueryRoot.get("termId")).value(termId);
            subquery.select(subqueryRoot.get("id")).where(sectionClasssPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }
}
