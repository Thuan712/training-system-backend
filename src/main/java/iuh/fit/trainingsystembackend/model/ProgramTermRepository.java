package iuh.fit.trainingsystembackend.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramTermRepository extends JpaRepository<ProgramTerm, Long> {
    List<ProgramTerm> findByProgramId(Long programId);
}