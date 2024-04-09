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
import java.util.Date;
import java.util.List;

@Data
@Table(name = "program")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Program implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Năm học
    @Column(name = "academic_year_id")
    private Long academicYearId;

    @ManyToOne
    @JoinFormula(value = "academic_year_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private AcademicYear academicYear;

    // Chuyên ngành
    @Column(name = "specialization_id")
    private Long specializationId;

    @ManyToOne
    @JoinFormula(value = "specialization_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Specialization specialization;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "deleted")
    private Boolean deleted;
}
