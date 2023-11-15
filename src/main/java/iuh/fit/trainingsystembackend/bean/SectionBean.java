package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.SectionType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionBean {

    //Section
    private Long id;

    private Long courseId;

    private Long termId;

    private String name;

    private String code;

    private Integer theoryPeriods;

    private Integer practicePeriods;

    private SectionType sectionType;

    private Boolean deleted;

}