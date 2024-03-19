package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.enums.CourseType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean implements Serializable {
    private Long id;
    private Long specializationId;
    private String name;
    private String code;
    private String description;
    private Boolean deleted = false;
}
