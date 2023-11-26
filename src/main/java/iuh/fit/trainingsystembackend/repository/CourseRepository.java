package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    boolean existsCourseByCode(String code);
}
