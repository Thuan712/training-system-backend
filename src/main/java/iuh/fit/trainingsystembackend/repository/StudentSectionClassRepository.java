package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSectionClassRepository extends JpaRepository<StudentSectionClass, Long> {
    int countAllBySectionClassId(Long sectionClassId);
}
