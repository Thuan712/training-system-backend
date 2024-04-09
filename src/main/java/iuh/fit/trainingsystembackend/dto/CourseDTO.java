package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseDTO {
    private Long id;
    private Long specializationId;
    private String specializationName;
    private String specializationCode;

    private String name;
    private String code;
    private String description;
    private CourseDuration courseDuration;
    private String courseDurationValue;

    private List<String> termRegister;

    private RequireCourse requireCourse;
    private String requireCourseValue;

    private Integer credits;
    private Double costCredits;
    private TypeOfKnowledge typeOfKnowledge;
    private CourseType courseType;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private Boolean deleted;
}
