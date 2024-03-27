package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {
    private Long studentId;
    private Long sectionClassId;
    private Date today = new Date();

    // Filter
    private List<Long> lecturerIds;
    private List<Long> termIds;
    private List<Long> sectionIds;
    private String searchValue;

}
