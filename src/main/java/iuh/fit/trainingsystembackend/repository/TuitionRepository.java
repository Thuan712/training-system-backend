package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Tuition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TuitionRepository extends JpaRepository<Tuition, Long>, JpaSpecificationExecutor<Tuition> {
}
