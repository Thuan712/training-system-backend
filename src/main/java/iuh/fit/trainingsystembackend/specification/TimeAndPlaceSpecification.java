package iuh.fit.trainingsystembackend.specification;

import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.request.TimeAndPlaceRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TimeAndPlaceSpecification extends BaseSpecification<TimeAndPlace, TimeAndPlaceRequest> {
    public Specification<TimeAndPlace> getFilter(TimeAndPlaceRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeEqual("sectionClassId", request.getSectionClassId()))
                        .toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private Specification<TimeAndPlace> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<TimeAndPlace> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

}
