package iuh.fit.trainingsystembackend.specification;


import iuh.fit.trainingsystembackend.common.specification.BaseSpecification;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.request.UserRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification extends BaseSpecification<UserEntity, UserRequest> {
    @Override
    public Specification<UserEntity> getFilter(UserRequest request) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Specification.where(attributeContains("username", request.getUsername()))
                .and(attributeContains("email", request.getEmail()))
                .and(attributeContains("firstName", request.getFirstname()))
                .and(attributeContains("lastName", request.getLastname()))
                .and(attributeEqual("systemRole", request.getSystemRole()))
                .and(attributeContains("code", request.getCode()))
                .and(attributeContains("dob", request.getDob()))
                .and(attributeContains("CINumber", request.getCINumber()))
                .and(attributeContains("avatar", request.getAvatar()))
                .and(attributeEqual("active", request.getActive()))
                .and(attributeEqual("deleted", request.getDeleted()))
                .toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private Specification<UserEntity> attributeContains(String key, String value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null || value.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), containsLowerCase(value));
        });
    }

    private Specification<UserEntity> attributeEqual(String key, Object value) {
        return((root, query, criteriaBuilder) -> {
            if(value == null){
                return null;
            }
            return criteriaBuilder.equal(root.get(key), value);
        });
    }

}
