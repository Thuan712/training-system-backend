package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean implements Serializable {
    private Long id;
    private Long specializationId;

    private String name;
    private String code; // Generate Auto
    private String description;
    private CourseDuration courseDuration;
    private List<TermType> termRegister;
    private RequireCourse requireCourse;
    private Integer credits;
    private Double costCredits;
    private TypeOfKnowledge typeOfKnowledge;
    private CourseType courseType;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private Boolean deleted = false;
}
