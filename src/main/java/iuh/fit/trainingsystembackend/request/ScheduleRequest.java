package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRequest {
    private List<Long> sectionClassIds;
    private List<Long> lecturerIds;

    private Long lecturerId;
    private Long studentId;
    private Long termId;
    private Long sectionClassId;
    private Long studentSectionClassId;
    private Date learningDate;
}
