package iuh.fit.trainingsystembackend.dto;

import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.enums.SectionType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionDTO {
    @Expose
    private Long id;

    private Long courseId;

    private String courseName;

    private String courseCode;
    private Integer credit;
    private List<Long> requireCourse;

    private Long termId;

    private String termName;

    private String name;

    private String code;

    private Integer theoryPeriods;

    private Integer practicePeriods;

    private String sectionType;

    private Date createdAt = new Date();

    private Date updatedAt;

    private Date deletedAt;

    private Boolean deleted;
}
