package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseRequest {
    private Long specializationId;

    private String name;
    private String code;
    private String description;
    private CourseDuration courseDuration;
    private List<TermType> termRegister;
    private RequireCourse requireCourse;
    private Integer credits;
    private Double costCredits;
    private TypeOfKnowledge typeOfKnowledge;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private Boolean deleted = false;
}
