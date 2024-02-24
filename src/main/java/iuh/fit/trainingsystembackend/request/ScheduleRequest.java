package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRequest {
    private Long sectionClassId;
    private Long studentSectionClassId;
    private Date learningDate;
}
