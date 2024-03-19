package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.data.SectionDuration;
import iuh.fit.trainingsystembackend.enums.SectionType;
import iuh.fit.trainingsystembackend.enums.TermType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionBean implements Serializable {

    private Long id;

    private Long specializationId;

    private List<Long> courseIds;

    private String name;

    private String description;

    private SectionDuration sectionDuration;

    private List<TermType> termRegister;

    public RequireSection requireSection;

    private Integer credits;

    private Double costCredits;

    private SectionType sectionType = SectionType.elective;

    private Boolean deleted;
}
