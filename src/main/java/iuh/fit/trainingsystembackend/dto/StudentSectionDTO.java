package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.CompletedStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.model.SectionClass;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentSectionDTO {
    private Long id;

    private Long studentId;
    private String studentName;
    private String studentCode;

    private Long termId;
    private String termName;

    private Long sectionId;
    private String sectionCode;
    private String sectionName;
    private SectionClassStatus sectionStatus;

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

    private RegistrationStatus registrationStatus;
    private CompletedStatus completedStatus;

    private List<SectionClass> sectionClasses;
}
