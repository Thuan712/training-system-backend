package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "specialization_class")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecializationClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @ManyToOne
    @JoinFormula(value = "lecturer_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Lecturer lecturer;

    @Column(name = "specialization_id")
    private Long specializationId;

    @ManyToOne
    @JoinFormula(value = "specialization_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Specialization specialization;

    @Column(name = "school_year")
    private String schoolYear;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_students")
    private Integer numberOfStudents;
}
