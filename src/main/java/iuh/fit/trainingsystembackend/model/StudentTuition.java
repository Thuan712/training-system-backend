package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
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
@Table(name = "student_tuition")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentTuition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Công nợ sinh viên
    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    // Học phí
    @Column(name = "tuition_fee_id")
    private Long tuitionId;

    @ManyToOne
    @JoinFormula(value = "tuition_fee_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Tuition tuition;

    // Khấu trừ (+)
    @Column(name = "plus_deductions")
    private Double plusDeductions;

    // Trừ nợ (-)
    @Column(name = "minus_deductions")
    private Double minusDeductions;

    @Column(name = "other_information")
    private String otherInformation;

    // Trạng thái đóng học phí
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TuitionStatus status;

    // Truy cứu công nợ
    @Column(name = "investigate_status")
    private Boolean investigateStatus;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
}
