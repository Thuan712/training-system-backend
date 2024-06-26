package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "course")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Chuyên ngành
    @Column(name = "specialization_id")
    private Long specializationId;

    @ManyToOne
    @JoinFormula(value = "specialization_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Specialization specialization;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    // Thời lượng các tiết học thành phần (Đơn vị tín chỉ tiết học)
    @Column(name = "course_duration")
    private String courseDurationString;

    @Transient
    private CourseDuration courseDuration;

    @Transient
    public CourseDuration getCourseDuration(){
        if(this.courseDurationString != null && !this.courseDurationString.isEmpty()) {
            return new Gson().fromJson(this.courseDurationString, new TypeToken<CourseDuration>(){}.getType());
        }

        return new CourseDuration();
    }

    //Học kì có thể đăng ký được môn học này
    @Column(name = "term_register")
    private String termRegisterString;

    @Transient
    private List<TermType> termRegister;

    @Transient
    public List<TermType> getTermRegister(){
        if(this.termRegisterString != null && !this.termRegisterString.isEmpty()) {
            return new Gson().fromJson(this.termRegisterString, new TypeToken<List<TermType>>(){}.getType());
        }

        return new ArrayList<>();
    }

    // Danh sách các điều kiện tiên quyết
    @Column(name = "require_course")
    private String requireCourseString;

    @Transient
    private RequireCourse requireCourse;

    @Transient
    public RequireCourse getRequireCourse(){
        if(this.requireCourseString != null && !this.requireCourseString.isEmpty()) {
            return new Gson().fromJson(this.requireCourseString, new TypeToken<RequireCourse>(){}.getType());
        }

        return new RequireCourse();
    }

    // Tín chỉ học tập (Tích luỹ cho chương trình đào tạo sau khi hoàn thành môn học)
    @Column(name = "credits")
    private Integer credits;

    // Tín chỉ học phí (Tín chỉ để tín ra học phí cho môn học)
    @Column(name = "cost_credits")
    private Double costCredits;

    // Loại nội dung kiến thức (Đại cương, Chuyên ngành)
    @Column(name = "type_of_knowlegde")
    @Enumerated(EnumType.STRING)
    private TypeOfKnowledge typeOfKnowledge;

    // Loại môn học (Bắt buộc, tự chọn)
    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date deletedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
