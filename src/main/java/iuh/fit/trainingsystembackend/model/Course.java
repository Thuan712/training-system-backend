package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "credit")
    private int credit;

    @Column(name = "require_course")
    @JsonIgnore
    private String requireCourseString;

    @Transient
    public List<Long> requireCourse;

    @Transient
    public List<Long> getRequireCourse(){
        if(this.requireCourseString != null && !this.requireCourseString.isEmpty()) {
            return new Gson().fromJson(this.requireCourseString, new TypeToken<List<Long>>(){}.getType());
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
    private Date deletedAt = new Date();

    @Column(name = "deleted")
    private Boolean deleted = false;
}
