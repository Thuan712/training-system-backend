package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
}
