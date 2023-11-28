package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
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
    private SectionClassType sectionClassType;
    private String note;
    private String startedAt;
    private DayInWeek dayInWeek;
    // Result
    private Long studentId;
    private List<Result> results = new ArrayList<>();
}
