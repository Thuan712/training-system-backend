package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentDTO {
    private Long id;

    private Long specializationClassId;
    private String specializationClassName;
    private Long specializationId;
    private String specializationName;

    private Long userId;
    private String name;
    private String code;
    private String dob;

    private String schoolYear;

    private TypeOfEducation typeOfEducation;

    private Date entryDate = new Date();
}
