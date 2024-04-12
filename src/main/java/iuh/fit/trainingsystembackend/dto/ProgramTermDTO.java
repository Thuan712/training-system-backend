package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.ProgramTermType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.model.ProgramCourse;
import iuh.fit.trainingsystembackend.model.ProgramTerm;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramTermDTO {
    private Long id;
    private Long programId;
    private List<CourseDTO> programCourses;
    private Integer minimumElective;
    private Integer totalElective;
    private Integer totalCompulsory;
    private ProgramTermType termType;
}
