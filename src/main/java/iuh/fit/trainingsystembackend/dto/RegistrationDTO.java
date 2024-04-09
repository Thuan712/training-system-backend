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
}
