package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Table(name = "time_and_place")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeAndPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_class_id")
    private Long sectionClassId;

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
