package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.StudentTuition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTuitionRepository extends JpaRepository<StudentTuition, Long> {
    StudentTuition findByStudentIdAndTuitionId(Long studentId, Long tuitionId);
}
