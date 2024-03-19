package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @ManyToOne
    @JoinFormula(value = "lecturer_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Lecturer lecturer;

    @Column(name = "term_id")
    private Long termId;

    @ManyToOne
    @JoinFormula(value = "term_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Term term;

    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne
    @JoinFormula(value = "section_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Section section;
    
    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "code")
    private String code;

    @Column(name = "number_of_students")
    private Integer numberOfStudents;

    @Column(name = "note")
    private String note;

    @Column(name = "section_class_type")
    @Enumerated(EnumType.STRING)
    private SectionClassType sectionClassType = SectionClassType.theory;

    @Column(name = "section_class_status")
    @Enumerated(EnumType.STRING)
    private SectionClassStatus sectionClassStatus = SectionClassStatus.open;
}
