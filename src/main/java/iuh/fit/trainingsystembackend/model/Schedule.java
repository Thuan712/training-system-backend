package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.ScheduleType;
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

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @ManyToOne
    @JoinFormula(value = "lecturer_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Lecturer lecturer;

    @Column(name = "time_and_place_id")
    private Long timeAndPlaceId;

    @ManyToOne
    @JoinFormula(value = "time_and_place_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private TimeAndPlace timeAndPlace;

    @Column(name = "learning_date")
    private Date learningDate;

    @Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name = "room")
    private String room;

    @Column(name = "day_of_the_week")
    @Enumerated(EnumType.STRING)
    private DayInWeek dayOfTheWeek;

    @Column(name = "period_start")
    private Integer periodStart;

    @Column(name = "period_end")
    private Integer periodEnd;

    @Column(name = "note")
    private String note;
}
