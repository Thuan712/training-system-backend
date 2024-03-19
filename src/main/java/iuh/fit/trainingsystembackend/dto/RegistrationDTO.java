package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistrationDTO {
    private Long id;
    private Long studentId;

    private Long sectionId;
    private String sectionName;
    private String sectionCode;
    private int credits;
    private Double costCredits;

    private Long sectionClassId;
    private String sectionClassCode;
    private SectionClassStatus sectionClassStatus;

    private Long lecturerId;
    private String lecturerName;

    private Double total;
    private RegistrationStatus status;
    private RegistrationType type;
    private Date createdAt;
}
