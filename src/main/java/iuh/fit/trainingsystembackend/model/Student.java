package iuh.fit.trainingsystembackend.model;

import iuh.fit.trainingsystembackend.enums.TrainingLevel;
import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "user_id")
    private Long userId;
//    @Column(name = "training_level")
//    @Enumerated(EnumType.STRING)
//    private TrainingLevel trainingLevel;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "type_of_education")
    @Enumerated(EnumType.STRING)
    private TypeOfEducation typeOfEducation;

    @Column(name = "entry_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate = new Date();

}
