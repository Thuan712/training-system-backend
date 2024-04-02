package iuh.fit.trainingsystembackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import jdk.jfr.Timestamp;
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
@Table(name = "student_section_class")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentSectionClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    @Column(name = "section_class_id")
    private Long sectionClassId;

    @ManyToOne
    @JoinFormula(value = "section_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private SectionClass sectionClass;

    @Column(name = "term_id")
    private Long termId;

    @ManyToOne
    @JoinFormula(value = "term_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Term term;

    @Column(name = "time_and_place_id")
    private Long timeAndPlaceId;

    @ManyToOne
    @JoinFormula(value = "time_and_place_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private TimeAndPlace timeAndPlace;

    @Column(name = "tuition_fee_id")
    private Long tuitionId;

    @ManyToOne
    @JoinFormula(value = "tuition_fee_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Tuition tuition;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    @Column(name = "registration_type")
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt = new Date();
}
