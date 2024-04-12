package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.ProgramTermType;
import iuh.fit.trainingsystembackend.model.Course;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramTermBean implements Serializable {
    private Long id;
    private Integer minimumElective;
    private ProgramTermType termType;
    private List<Course> programCourses = new ArrayList<>();
}
