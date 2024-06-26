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

    // Sinh viên đăng ký lớp học phần
    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinFormula(value = "student_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Student student;

    // Lớp học phần sinh viên đăng ký
    @Column(name = "section_class_id")
    private Long sectionClassId;

    @ManyToOne
    @JoinFormula(value = "section_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private SectionClass sectionClass;

    // Lịch học của lớp học phần đó
    @Column(name = "time_and_place_id")
    private Long timeAndPlaceId;

    @ManyToOne
    @JoinFormula(value = "time_and_place_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private TimeAndPlace timeAndPlace;

    // Học phần đăng ký
    @Column(name = "student_section_id")
    private Long studentSectionId;

    @ManyToOne
    @JoinFormula(value = "student_section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private StudentSection studentSection;
}
