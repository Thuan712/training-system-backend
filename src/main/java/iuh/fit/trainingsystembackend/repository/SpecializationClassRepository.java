package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.SpecializationClass;
import iuh.fit.trainingsystembackend.request.SpecializationClassRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationClassRepository extends JpaRepository<SpecializationClass, Long> , JpaSpecificationExecutor<SpecializationClass> {
    SpecializationClass findFirstByOrderBySpecializationIdDesc ();
}
