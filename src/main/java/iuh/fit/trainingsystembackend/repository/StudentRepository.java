package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.request.StudentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Student getStudentByUserId(Long userId);

    long countBySpecializationClassId(Long specializationClassId);

    Student findByUserId(Long userId);
}
