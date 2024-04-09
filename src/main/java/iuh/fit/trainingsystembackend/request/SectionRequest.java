package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.CourseType;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionRequest {
    private List<Long> excludeIds;
    private Long courseId;
    private Long studentId;
    private Long termId;
    private String name;
    private String code;
    private CourseType courseType;
    private Boolean deleted;
}
