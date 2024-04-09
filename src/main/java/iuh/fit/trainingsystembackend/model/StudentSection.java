package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.CompletedStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
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
@Table(name = "student_section")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentSection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Sinh viên đăng ký
    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    // Học phần đăng ký
    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne
    @JoinFormula(value = "section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Section section;

    // Kết quả
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne
    @JoinFormula(value = "result_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Result result;

    // Trạng thái đăng ký
    @Column(name = "registration_status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    // Trạng thái hoàn thành học phần
    @Column(name = "completed_status")
    @Enumerated(EnumType.STRING)
    private CompletedStatus completedStatus;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt = new Date();
}
