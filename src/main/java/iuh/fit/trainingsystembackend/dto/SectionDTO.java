package iuh.fit.trainingsystembackend.dto;

import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import iuh.fit.trainingsystembackend.model.Term;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionDTO {
    @Expose
    private Long id;
    private String name;
    private String fullName;
    private String code;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean deleted;
    private Date lockDate;
    private Date openDate;

    // Term
    private Long termId;
    private String termName;

    // Specialization
    private Long specializationId;
    private String specializationName;
    private String specializationCode;

    // Course
    private Long courseId;
    private String courseName;
    private String courseCode;
    private CourseDuration courseDuration;
    private String courseDurationValue;
    private List<TermType> termRegister;
    private RequireCourse requireCourse;
    private Integer credits;
    private Double costCredits;
    private TypeOfKnowledge typeOfKnowledge;
    private CourseType courseType;


}
