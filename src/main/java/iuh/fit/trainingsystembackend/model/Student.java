package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.TrainingLevel;
import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
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
@Table(name = "student")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization_class_id")
    private Long specializationClassId;

    @ManyToOne
    @JoinFormula(value = "specialization_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private SpecializationClass specializationClass;

    @Column(name = "specialization_id")
    private Long specializationId;

    @ManyToOne
    @JoinFormula(value = "specialization_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Specialization specialization;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinFormula(value = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private UserEntity userEntity;

    @Column(name = "school_year")
    private String schoolYear;

    @Column(name = "type_of_education")
    @Enumerated(EnumType.STRING)
    private TypeOfEducation typeOfEducation;

    @Column(name = "activation_email")
    private String activationEmail;

    @Column(name = "entry_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate = new Date();
}
