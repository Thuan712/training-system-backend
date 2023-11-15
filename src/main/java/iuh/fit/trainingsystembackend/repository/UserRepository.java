package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByUsername(String findByUsername);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByCode(String code);
    UserEntity findUserEntityByUsernameOrEmail(String username, String email);
}
