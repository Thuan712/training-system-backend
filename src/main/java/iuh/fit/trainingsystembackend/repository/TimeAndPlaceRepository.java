package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeAndPlaceRepository extends JpaRepository<TimeAndPlace, Long> {
    List<TimeAndPlace> findBySectionClassId(Long sectionId);
    long deleteAllBySectionClassId(Long sectionClassId);
}
