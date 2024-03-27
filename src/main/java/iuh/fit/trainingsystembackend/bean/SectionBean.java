package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.data.SectionDuration;
import iuh.fit.trainingsystembackend.enums.SectionType;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.enums.TypeOfKnowledge;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionBean implements Serializable {

    private Long id;

    private Long specializationId;

    private Long courseId;

    private String name;

    private String description;

    private SectionDuration sectionDuration;

    private List<TermType> termRegister;

    public RequireSection requireSection;

    private Integer credits;

    private Double costCredits;

    private SectionType sectionType = SectionType.elective;
    private TypeOfKnowledge typeOfKnowledge = TypeOfKnowledge.general_education;
    private Boolean deleted;
}
