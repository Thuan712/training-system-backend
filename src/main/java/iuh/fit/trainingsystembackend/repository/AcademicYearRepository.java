package iuh.fit.trainingsystembackend.repository;

import feign.Param;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    boolean existsByYearStart(int yearStart);

    AcademicYear findByYearStart(Integer yearStart);
}
