package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    Term findDistinctByName(String name);
}
