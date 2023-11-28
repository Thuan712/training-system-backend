package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.RefType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "schedule")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_class_id")
    private Long sectionClassId;

    @ManyToOne
    @JoinFormula(value = "section_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private SectionClass sectionClass;

    @Column(name = "student_section_class_id")
    private Long studentSectionClassId;

    @ManyToOne
    @JoinFormula(value = "student_section_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private StudentSectionClass studentSectionClass;

    @Column(name = "learning_date")
    private Date learningDate;
}
