package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {
    private Long studentId;
    private Long sectionClassId;
    private Long termId;
}
