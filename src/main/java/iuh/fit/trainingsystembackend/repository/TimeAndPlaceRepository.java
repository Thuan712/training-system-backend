package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeAndPlaceRepository extends JpaRepository<TimeAndPlace, Long>, JpaSpecificationExecutor<TimeAndPlace> {
    List<TimeAndPlace> findBySectionClassId(Long sectionId);
    List<TimeAndPlace> findByRoomAndDayOfTheWeek(String room, DayInWeek dayOfTheWeek);

    boolean existsBySectionClassId(Long sectionClassId);

    List<TimeAndPlace> findBySectionClassIdNot(Long sectionClassId);
}
