package iuh.fit.trainingsystembackend.model;


import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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

    @Column(name = "section_class_id")
    private Long sectionClassId;

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
