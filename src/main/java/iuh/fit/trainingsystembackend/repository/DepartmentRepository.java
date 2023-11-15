package iuh.fit.trainingsystembackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Department extends JpaRepository<Department, Long> {
}
