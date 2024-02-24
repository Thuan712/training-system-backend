package iuh.fit.trainingsystembackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
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
    private Date learningDate;

    // Section Class
    private Long lecturerId;

    private String lecturerName;

    private Long sectionId;

    private String sectionName;

    private String sectionCode;

    private String classCode;

    private String room;

    private Integer periodFrom;

    private Integer periodTo;

    private Integer numberOfStudents;

    private String dayInWeek;

    private String note;

    private String sectionClassType;

    private Date startedAt;
    private String dateTime;
}
