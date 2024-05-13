package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.request.LecturerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long>, JpaSpecificationExecutor<Lecturer> {
    Lecturer getLecturersByUserId(Long userId);

    Lecturer findByUserId(Long userId);
}
