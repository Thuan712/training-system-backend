package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentRequest {
    private Long userId;
    private Long sectionClassId;
    private Long specializationClassId;
    private Long specializationId;
    private String schoolYear;
    private TypeOfEducation typeOfEducation;
}
