package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionClassRequest {

    private Long lecturerId;

    private Long sectionId;

    private String classCode;

    private String room;

    private Integer periodFrom;

    private Integer periodTo;

    private String note;
}
