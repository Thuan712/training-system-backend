package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.CompletedStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Table(name = "student_course")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinFormula(value = "course_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Course course;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne
    @JoinFormula(value = "result_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Result result;

    @Column(name = "completed_status")
    @Enumerated(EnumType.STRING)
    private CompletedStatus completedStatus;
}
