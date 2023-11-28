package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClassDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lecturerId;
    private String lecturerName;
    private String lecturerCode;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    private String classCode;

    private String room;

    private Integer periodFrom;

    private Integer periodTo;

    private Integer numberOfStudents;

    private DayInWeek dayInWeek;

    private String note;

    private SectionClassType sectionClassType;
    private Integer registered;
    private SectionClassStatus sectionClassStatus;
    private Date startedAt;
}
