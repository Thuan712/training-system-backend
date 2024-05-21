package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>, JpaSpecificationExecutor<Program> {
    Program findBySpecializationId(Long specializationId);

    Program findBySpecializationIdAndAcademicYearId(Long specializationId, Long academicYearId);

    Program findFirstBySpecializationIdOrderByCreatedAtDesc(Long specializationId);
}
