package iuh.fit.trainingsystembackend.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {
    List<ProgramCourse> findByProgramTermId(Long programTermId);
}