package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassRequest {
    private Long specializationClassId;
    private Long termId;
    private Long lecturerId;
    private Long studentId;
    private Long sectionId;
    private String code;
    private String note;
    private SectionClassType sectionClassType;
    private Boolean sectionClassRef;
    private Long sectionClassId;
    private Boolean createStatus;

    // Filter
    private String searchValue;
    private List<Long> sectionIds;
    private List<Long> lecturerIds;
    private List<Long> termIds;
}
