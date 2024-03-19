package iuh.fit.trainingsystembackend.dto;

import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.data.SectionDuration;
import iuh.fit.trainingsystembackend.enums.SectionType;
import iuh.fit.trainingsystembackend.enums.TermType;
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

    private Long specializationId;

    private String specializationName;

    private String specializationCode;

    private List<Long> courseIds;

    private String name;

    private String code;

    private String description;

    private List<String> prerequisite;

    private List<String> studyFirst;

    private List<String> parallel;

    private RequireSection requireSection;

    private SectionDuration sectionDuration;

    private int theory;

    private int practice;

    private int discussionExercises;

    private int selfLearning;

    private List<TermType> termRegister;

    private Integer credits;

    private Double costCredits;

    private SectionType sectionType;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Boolean deleted;
}
