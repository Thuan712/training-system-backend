package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.SectionType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionBean implements Serializable {

    //Section
    private Long id;

    private Long courseId;

    private Long termId;

    private String name;

    private String code;

    private Integer theoryPeriods;

    private Integer practicePeriods;

    private SectionType sectionType = SectionType.elective;

    private Boolean deleted;
//
//    // Section Class
//    private Long sectionClassId;
//    private Long lecturerId;
//    private Long sectionId;
//    private String classCode;
//    private String room;
//    private Integer periodTo;
//    private Integer periodFrom;
//    private String note;
}
