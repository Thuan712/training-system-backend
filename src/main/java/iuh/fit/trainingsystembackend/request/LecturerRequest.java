package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import iuh.fit.trainingsystembackend.enums.TrainingLevel;
import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LecturerRequest {
    private Long userId;
    private Long specificationId;
    private Position position;
    private Title title;
}
