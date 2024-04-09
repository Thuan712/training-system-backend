package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassBean {
    private Long id;
    private Long lecturerId;
    private Long sectionId;
    private Long refId;

    private String code;
    private Integer maxStudents;
    private Integer minStudents;

    private SectionClassType sectionClassType;

    private String note;
    private Boolean deleted = false;

    // Thông tin thời gian học
    private List<TimeAndPlace> timeAndPlaces;

    // Change status section Class for registration
    private SectionClassStatus sectionClassStatus = SectionClassStatus.open;
}
