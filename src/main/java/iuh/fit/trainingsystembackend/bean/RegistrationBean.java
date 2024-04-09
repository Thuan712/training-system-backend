package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationBean {
    private Long id;

    private Long studentId;
    private Long sectionClassTheoryId;
    private Long timeAndPlaceTheoryId;

    private Long sectionClassPracticeId;
    private Long timeAndPlacePracticeId;

    private RegistrationStatus registrationStatus = RegistrationStatus.new_learning;

    private boolean isPracticeClass = false;
}
