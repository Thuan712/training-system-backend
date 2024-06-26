package iuh.fit.trainingsystembackend.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecializationClassDTO {
    private Long id;

    private Long specializationId;
    private String specializationName;
    private String specializationCode;

    private Long lecturerId;
    private String lecturerName;
    private String lecturerCode;

    private String schoolYear;
    private Integer numberOfStudents;

    private String name;
}
