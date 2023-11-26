package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.model.Result;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassBean {
    private Long id;
    private Long lecturerId;
    private Long sectionId;
    private String classCode;
    private String room;
    private Integer periodTo;
    private Integer periodFrom;
    private Integer numberOfStudents;
    private String note;
    // Result
    private List<Result> results = new ArrayList<>();
}
