package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.ProgramTermType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Table(name = "program_term")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "program_id")
    private Long programId;

    @ManyToOne
    @JoinFormula(value = "program_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Program program;

    @Column(name = "total_elective")
    private Integer totalElective;

    @Column(name = "total_compulsory")
    private Integer totalCompulsory;

    @Column(name = "program_term")
    @Enumerated(EnumType.STRING)
    private ProgramTermType programTermType;
}
