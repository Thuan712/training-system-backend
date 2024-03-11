package iuh.fit.trainingsystembackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.enums.CourseType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseDTO {
    private Long id;

    private Long specializationId;

    private String name;

    private String code;

    private String description;

    private int credit;

    private CourseType courseType;

    public List<Long> prerequisite;

    private Date createdAt;

    private Date deletedAt;

    private Boolean deleted;
}
