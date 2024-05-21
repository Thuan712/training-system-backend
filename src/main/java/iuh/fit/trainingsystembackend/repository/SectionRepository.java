package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long>, JpaSpecificationExecutor<Section> {
    Section findSectionByCode(String code);

    Section findByCourseIdAndTermId(Long courseId, Long termId);

    List<Section> findByTermId(Long termId);
}
