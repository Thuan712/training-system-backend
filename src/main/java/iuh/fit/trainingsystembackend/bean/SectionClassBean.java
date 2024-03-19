package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassBean {
    private Long id;
    private Long termId;
    private Long lecturerId;
    private Long sectionId;
    private Long refId;
    private String code;
    private Integer numberOfStudents;
    private List<TimeAndPlace> timeAndPlaces;
    private SectionClassType sectionClassType = SectionClassType.theory;
    private String note;

    // Result
    // private Long studentId;
    // private List<Result> results = new ArrayList<>();

    // Register Section
    private Long studentId;
    private Long sectionClassTheoryId;
    private Long sectionClassPracticeId;
    private Long timeAndPlacePracticeId;
    private Long timeAndPlaceTheoryId;

    // Cancel Section
    private Long studentSectionClassId;
}
