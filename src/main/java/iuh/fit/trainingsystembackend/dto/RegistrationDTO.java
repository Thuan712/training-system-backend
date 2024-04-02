package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.model.Schedule;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistrationDTO {
    private Long id;
    private Long studentId;

    private Long termId;
    private String termName;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;
    private int credits;
    private Double costCredits;

    private Long sectionClassId;
    private String sectionClassCode;
    private SectionClassStatus sectionClassStatus;
    private SectionClassType sectionClassType;

    private Long lecturerId;
    private String lecturerName;
    private String lecturerCode;

    private Long timeAndPlaceId;
    private String timeAndPlaceName;

    private RegistrationStatus status;
    private RegistrationType type;

    private List<Schedule> scheduleList;
    private Date createdAt;

    // Tuition
    private Long tuitionId;

    private Double initialFee;
    private Double debt;
    private Double discountAmount;
    private Double discountFee;

    private Double plusDeductions;

    private Double minusDeductions;

    private String otherInformation;

    private TuitionStatus tuitionStatus;

    private Double total;
}
