package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRequest {
    private List<Long> sectionClassIds;
    private Long  sectionClassId;
    private Long studentSectionClassId;
    private Date learningDate;
}
