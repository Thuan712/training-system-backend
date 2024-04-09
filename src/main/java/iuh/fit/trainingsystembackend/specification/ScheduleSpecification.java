package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.request.ScheduleRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@Component
public class ScheduleSpecification extends BaseSpecification<Schedule, ScheduleRequest> {
    @Override
    public Specification<Schedule> getFilter(ScheduleRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("sectionClassId", request.getSectionClassId()))
                        .and(attributeEqual("learningDate", request.getLearningDate()))
                        .and(attributeLecturerIdEqual(request.getLecturerId()))
                        .and(attributeSectionClassIdsIn(request.getSectionClassIds()))
                        .toPredicate(root, query, criteriaBuilder);
    }


    private Specification<Schedule> attributeContains(String key, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Schedule> attributeEqual(String key, Object value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

    private Specification<Schedule> attributeSectionClassIdsIn(List<Long> sectionClassIds) {
        return ((root, query, criteriaBuilder) -> {
            if (sectionClassIds == null || sectionClassIds.isEmpty()) {
                return null;
            }

            return criteriaBuilder.in(root.get("sectionClassId")).value(sectionClassIds);
        });
    }

    private Specification<Schedule> attributeLecturerIdEqual(Long lecturerId) {
        return((root, query, criteriaBuilder) -> {
            if (lecturerId == null) {
                return null;
            }

            Subquery<SectionClass> subquery = query.subquery(SectionClass.class);
            Root<SectionClass> subqueryRoot = subquery.from(SectionClass.class);

            Predicate sectionClasssPredicate = criteriaBuilder.in(subqueryRoot.get("leturerId")).value(lecturerId);
            subquery.select(subqueryRoot.get("id")).where(sectionClasssPredicate);

            return criteriaBuilder.in(root.get("sectionClassId")).value(subquery);
        });
    }
}


