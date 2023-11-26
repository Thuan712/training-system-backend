package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.SectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionClassRepository extends JpaRepository<SectionClass, Long>, JpaSpecificationExecutor<SectionClass> {
    boolean existsSectionClassByClassCode(String classCode);
    boolean existsSectionClassByLecturerIdAndSectionId(Long lecturerId, Long sectionId);
}
