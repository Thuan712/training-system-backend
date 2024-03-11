package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.enums.SectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "course")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "credit")
    private int credit;

    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @Column(name = "prerequisite")
    @JsonIgnore
    private String prerequisiteString;

    @Transient
    public List<Long> prerequisite;

    @Transient
    public List<Long> getPrerequisite(){
        if(this.prerequisiteString != null && !this.prerequisiteString.isEmpty()) {
            return new Gson().fromJson(this.prerequisiteString, new TypeToken<List<Long>>(){}.getType());
        }

        return new ArrayList<>();
    }


    @Column(name = "created_at")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "deleted_at")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
