package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationSectionBean implements Serializable {
    private Long id;
    private Long termId;
    private Long sectionId;
    private Long sectionClassId;
    private Long timeAndPlaceId;
    private RegistrationType type;
    private RegistrationStatus status;
    private Long studentId;
}
