package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.TuitionStatus;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentTuitionDTO {
    private Long id;

    // Term
    private Long termId;
    private String termName;

    // Section
    private Long sectionId;
    private String sectionName;
    private String sectionCode;

    // Student
    private Long studentId;
    private String studentName;
    private String studentCode;

    // Tuition
    private Long tuitionFeeId;
    private Double initialFee;
    private Double discountAmount = 0D;
    private Double discountFee = 0D;
    private Date paymentDeadline;

    // Student Section Class
    // Student Section

    // Student Tuition
    private TuitionStatus status;
    private Date paymentDate;
}
