package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByStudentId(Long studentId);

    Result findResultBySectionIdAndStudentId(Long sectionId, Long studentId);
}
