package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.SectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionClassRepository extends JpaRepository<SectionClass, Long>, JpaSpecificationExecutor<SectionClass> {
    boolean existsSectionClassByCode(String code);
    boolean existsSectionClassByLecturerIdAndSectionId(Long lecturerId, Long sectionId);

    List<SectionClass> findSectionClassBySectionIdAndLecturerId(Long sectionId, Long lecturerId);
}
