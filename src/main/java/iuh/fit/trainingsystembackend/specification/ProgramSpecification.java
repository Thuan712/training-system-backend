package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Program;
import iuh.fit.trainingsystembackend.request.LecturerRequest;
import iuh.fit.trainingsystembackend.request.ProgramRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProgramSpecification extends BaseSpecification<Program, ProgramRequest> {

    @Override
    public Specification<Program> getFilter(ProgramRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("academicYearId",request.getAcademicYearId())
                        .and(attributeEqual("specializationId", request.getSpecializationId()))
                        .and(attributeContains("name", request.getName()))
                ).toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Program> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Program> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
