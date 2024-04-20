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
@Table(name = "result")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Result implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne
    @JoinFormula(value = "section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Section section;

    @Column(name = "regular_point_1")
    private Double regularPoint1;

    @Column(name = "regular_point_2")
    private Double regularPoint2;

    @Column(name = "regular_point_3")
    private Double regularPoint3;

    @Column(name = "regular_point_4")
    private Double regularPoint4;

    @Column(name = "regular_point_5")
    private Double regularPoint5;

    @Column(name = "midterm_point_1")
    private Double midtermPoint1;

    @Column(name = "midterm_point_2")
    private Double midtermPoint2;

    @Column(name = "midterm_point_3")
    private Double midtermPoint3;

    @Column(name = "final_point")
    private Double finalPoint;

    @Column(name = "practice_point_1")
    private Double practicePoint1;

    @Column(name = "practice_point_2")
    private Double practicePoint2;
}
