package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultBean implements Serializable {


    // Result
    private Long id;
    private Long studentId;

    private String studentName;

    private String studentCode;

    private Long sectionId;

    private Long courseId;

    private Double regularPoint1;

    private Double regularPoint2;

    private Double regularPoint3;

    private Double regularPoint4;

    private Double regularPoint5;

    private Double midtermPoint1;

    private Double midtermPoint2;

    private Double midtermPoint3;

    private Double finalPoint1;

    private Double practicePoint1;

    private Double practicePoint2;
}
