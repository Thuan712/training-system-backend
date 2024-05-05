package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentTuitionRequest {
    private Long studentId;
    private Long tuitionId;
    private TuitionStatus status;
}
