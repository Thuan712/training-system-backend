package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean implements Serializable {
    private Long id;
    private String name;
    private String code;
    private Integer credits;

    private List<Long> requireCourse;
    private Boolean deleted = false;
}
