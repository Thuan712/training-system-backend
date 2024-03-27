package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.data.SectionDuration;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.enums.SectionType;
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
import java.util.*;

@Data
@Table(name = "section")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Section implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    // Loại học phần (Bắt buộc, tự chọn)
    @Column(name = "section_type")
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinFormula(value = "course_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Course course;

    // Thời lượng các tiết học thành phần (Đơn vị tín chỉ tiết học)
    @Column(name = "section_duration")
    private String sectionDurationString;

    @Transient
    private SectionDuration sectionDuration;

    @Transient
    public SectionDuration getSectionDuration(){
        if(this.sectionDurationString != null && !this.sectionDurationString.isEmpty()) {
            return new Gson().fromJson(this.sectionDurationString, new TypeToken<SectionDuration>(){}.getType());
        }

        return new SectionDuration();
    }

    //Học kì có thể đăng ký được học phần này
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
    @Column(name = "require_section")
    private String requireSectionString;

    @Transient
    public RequireSection requireSection;

    @Transient
    public RequireSection getRequireSection(){
        if(this.requireSectionString != null && !this.requireSectionString.isEmpty()) {
            return new Gson().fromJson(this.requireSectionString, new TypeToken<RequireSection>(){}.getType());
        }

        return new RequireSection();
    }

    // Tín chỉ học tập (Tích luỹ cho chương trình đào tạo sau khi hoàn thành học phần)
    @Column(name = "credits")
    private Integer credits;

    // Tín chỉ học phí (Tín chỉ để tín ra học phí cho học phần)
    @Column(name = "cost_credits")
    private Double costCredits;

    // Loại nội dung kiến thức (Đại cương, Chuyên ngành)
    @Column(name = "type_of_knowlegde")
    @Enumerated(EnumType.STRING)
    private TypeOfKnowledge typeOfKnowledge;

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
    private Boolean deleted;
}
