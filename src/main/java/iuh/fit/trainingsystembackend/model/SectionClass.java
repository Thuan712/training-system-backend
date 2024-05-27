package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
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
import java.util.Date;

@Data
@Table(name = "section_class")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization_class_id")
    private Long specializationClassId;

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @ManyToOne
    @JoinFormula(value = "lecturer_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Lecturer lecturer;

    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne
    @JoinFormula(value = "section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Section section;

    // Lớp lý thuyết mà lớp thực hành thuộc về
    @Column(name = "ref_id")
    private Long refId;

    @ManyToOne
    @JoinFormula(value = "ref_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private SectionClass sectionClass;

    @Column(name = "code")
    private String code;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "min_students")
    private Integer minStudents;

    @Column(name = "note")
    private String note;

    // Loại lớp học phần
    @Column(name = "section_class_type")
    @Enumerated(EnumType.STRING)
    private SectionClassType sectionClassType = SectionClassType.theory;

    // Trạng thái tạo lớp học phần
    @Column(name = "create_status")
    private Boolean createStatus;

    // Trạng thái đã nộp kết quả
    @Column(name = "input_result_enable")
    private Boolean inputResultEnable = true;

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
