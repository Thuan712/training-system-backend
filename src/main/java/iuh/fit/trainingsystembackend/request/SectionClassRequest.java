package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassRequest {
    private Long termId;
    private Long lecturerId;
    private Long studentId;
    private Long sectionId;
    private String code;
    private String note;
    private SectionClassType sectionClassType;

}
