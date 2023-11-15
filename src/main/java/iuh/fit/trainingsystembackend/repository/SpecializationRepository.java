package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.request.SpecializationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long>, JpaSpecificationExecutor<Specialization> {
}
