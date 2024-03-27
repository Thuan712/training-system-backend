package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeAndPlaceRequest {
    private Long sectionClassId;
}
