package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
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
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClassDTO {
    private Long id;
    private String name;

    private Long termId;
    private String termName;

    private String code;
    private Integer maxStudents;
    private Integer minStudents;
    private String note;

    private Long refId;
    private String refSectionClassName;
    private String refSectionClassCode;

    private Long lecturerId;
    private String lecturerName;
    private String lecturerCode;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    private SectionClassType sectionClassType;
    private SectionClassStatus sectionClassStatus;

    private Integer numberOfStudents;
    private List<StudentDTO> students;

    private List<TimeAndPlace> timeAndPlaces;
    private Boolean timeAndPlaceStatus;
    private Date startDate;
    private Date endDate;
    private Integer numberOfPeriods;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean deleted;

    private Boolean createStatus;
    private Boolean inputResultEnable;

}
