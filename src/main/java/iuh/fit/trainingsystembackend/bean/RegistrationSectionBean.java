package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationSectionBean implements Serializable {
    // Section Registration
    private Long id;
    private Long termId;
    private Long studentId;
    private Long userId;
    private Long sectionId;
    private RegistrationStatus registrationStatus;

    // Change Status
    private RegistrationStatus status;

    // Section Class Registration
    private Long sectionClassRefId;
    private Long timeAndPlaceRefId;

    private Long sectionClassId;
    private Long timeAndPlaceId;

}
