package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean {
    private Long id;
    private String name;
    private String code;
    private int credits;

    private List<Long> requireCourse;
    private Boolean deleted = false;
}
