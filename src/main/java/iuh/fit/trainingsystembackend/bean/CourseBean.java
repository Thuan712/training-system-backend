package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.CourseType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean implements Serializable {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer credit;
    private CourseType courseType;
    private List<Long> prerequisite;
    private Boolean deleted = false;
}
