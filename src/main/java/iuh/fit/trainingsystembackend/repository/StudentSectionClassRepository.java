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
    StudentSectionClass findByStudentIdAndSectionClassId(Long studentId, Long sectionClassId);
    List<StudentSectionClass> findByStudentId(Long studentId);
    List<StudentSectionClass> findByStudentSectionId(Long studentSectionId);

    List<StudentSectionClass> findBySectionClassId(Long sectionClassId);

    StudentSectionClass findByStudentSectionIdAndStudentIdAndSectionClassId(Long studentSectionId, Long studentId, Long sectionClassId);
}
