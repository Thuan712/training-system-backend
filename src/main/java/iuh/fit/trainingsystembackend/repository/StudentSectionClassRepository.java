package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSectionClassRepository extends JpaRepository<StudentSectionClass, Long>, JpaSpecificationExecutor<StudentSectionClass> {
    int countAllBySectionClassId(Long sectionClassId);
    boolean existsByStudentIdAndSectionClassId(Long studentId, Long sectionClassId);

    List<StudentSectionClass> findByStudentIdAndTermId(Long studentId, Long termId);
    List<StudentSectionClass> findByStudentId(Long studentId);
}
