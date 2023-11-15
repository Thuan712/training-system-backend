package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseRequest {
    private String name;
    private String code;
    private int credit;
    private List<Long> requireCourses;
    private Boolean deleted = false;
}
