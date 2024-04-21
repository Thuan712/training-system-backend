package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.StudentSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSectionRepository extends JpaRepository<StudentSection, Long>, JpaSpecificationExecutor<StudentSection> {
    boolean existsByStudentIdAndSectionId(Long studentId, Long sectionId);

    List<StudentSection> findByStudentId(Long studentId);

    List<StudentSection> findBySectionId(Long sectionId);

    StudentSection findBySectionIdAndStudentId(Long sectionId, Long studentId);
}
