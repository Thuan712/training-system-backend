package iuh.fit.trainingsystembackend.model;

import iuh.fit.trainingsystembackend.enums.CompletedStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudentIdAndCompletedStatus(Long studentId, CompletedStatus completedStatus);

    StudentCourse findByCourseIdAndStudentId(Long courseId, Long studentId);
}