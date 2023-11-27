package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
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

@Data
@Table(name = "section_class")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne
    @JoinFormula(value = "section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Section section;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "room")
    private String room;

    @Column(name = "period_from")
    private Integer periodFrom;

    @Column(name = "period_to")
    private Integer periodTo;

    @Column(name = "number_of_students")
    private Integer numberOfStudents;

    @Column(name = "day_in_week")
    @Enumerated(EnumType.STRING)
    private DayInWeek dayInWeek;

    @Column(name = "note")
    private String note;

    @Column(name = "section_class_type")
    @Enumerated(EnumType.STRING)
    private SectionClassType sectionClassType;

    @Column(name = "started_at")
    private Date startedAt;
}
