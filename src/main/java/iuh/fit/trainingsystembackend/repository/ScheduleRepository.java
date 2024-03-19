package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    List<Schedule> findScheduleBySectionClassId(Long sectionClassId);
}
