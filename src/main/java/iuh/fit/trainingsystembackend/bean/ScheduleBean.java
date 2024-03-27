package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.ScheduleType;
import iuh.fit.trainingsystembackend.model.SectionClass;
import lombok.Data;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleBean {
    private Long id;

    private Long sectionClassId;
    private Long lecturerId;

    private Date learningDate;

    private ScheduleType scheduleType;


    private String room;

    private DayInWeek dayInWeek;

    private Integer periodStart;

    private Integer periodEnd;

    private String note;
}
