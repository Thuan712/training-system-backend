package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.SectionClass;
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
    private String studentName;
    private String studentCode;

    private Long termId;
    private String termName;

    private Long courseId;
    private String courseName;
    private String courseCode;
    private int credits;
    private Double costCredits;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    private RegistrationStatus registrationStatus;
    private Date createdAt;

    // Tuition
    private Long tuitionId;
//    private
    private Double initialFee;
    private Double debt;
    private Double discountAmount;
    private Double discountFee;


    // Tuition Student
    private Double plusDeductions;
    private Double minusDeductions;
    private String otherInformation;
    private Date paymentDeadline;
    private Double total;
    private TuitionStatus tuitionStatus;
    private Date paymentDate;
    private Boolean investigateStatus;
    // Result
    private Long resultId;
    private Double regularPoint1;
    private Double regularPoint2;
    private Double regularPoint3;
    private Double regularPoint4;
    private Double regularPoint5;

    private Double midtermPoint1;
    private Double midtermPoint2;
    private Double midtermPoint3;

    private Double finalPoint;
    private Double practicePoint1;
    private Double practicePoint2;

    private Double totalPoint;
    private CompletedStatus completedStatus;

    // Section Class Code
    private List<SectionClass> sectionClasses;
}
