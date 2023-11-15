package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.request.LecturerRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LecturerSpecification extends BaseSpecification<Lecturer, LecturerRequest> {

    @Override
    public Specification<Lecturer> getFilter(LecturerRequest request) {
        return (root, query, criteriaBuilder) ->
                Specification.where(attributeEqual("position",request.getPosition())
                        .and(attributeEqual("title", request.getTitle()))
                        .and(attributeEqual("userId", request.getUserId()))
                        .and(attributeEqual("specificationId", request.getSpecificationId()))
        ).toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Lecturer> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<Lecturer> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }
}
