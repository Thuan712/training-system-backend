package iuh.fit.trainingsystembackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.ScheduleType;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import iuh.fit.trainingsystembackend.model.Section;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScheduleDTO {
    // Schedule
    @Expose
    private Long id;
    private Long sectionClassId;
    private String sectionClassCode;
    private String learningDate;

    // Term
    private Long termId;
    private String termName;

    // Section Class
    private Long lecturerId;
    private String lecturerName;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    private String room;
    private Integer numberOfStudents;
    private DayInWeek dayInWeek;
    private String note;
    private String sectionClassType;
    private ScheduleType scheduleType;
    // Time And Place
    private int periodStart;
    private int periodEnd;
}
