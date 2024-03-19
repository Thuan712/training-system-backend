package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
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
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClassDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Long lecturerId;
    private String lecturerName;
    private String lecturerCode;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    private Long refId;
    private String code;
    private List<TimeAndPlace> timeAndPlaces;
    private Integer numberOfStudents;
    private String note;

    private SectionClassType sectionClassType;
    private SectionClassStatus sectionClassStatus;
}
