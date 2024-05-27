package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // Môn học
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinFormula(value = "course_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Course course;

    // Học kỳ
    @Column(name = "term_id")
    private Long termId;

    @ManyToOne
    @JoinFormula(value = "term_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Term term;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "open_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openDate;

    @Column(name = "lock_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockDate;

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
