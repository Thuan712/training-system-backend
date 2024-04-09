package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class
AcademicYearBean {
    private Long id;
    private Integer yearStart;
    private boolean active = true;

    private Long firstTermId;
    private String firstTermName;
    private Double costFirstTerm;
    private Date firstTermStart;
    private Date firstTermEnd;

    private Long secondTermId;
    private String secondTermName;
    private Double costSecondTerm;
    private Date secondTermStart;
    private Date secondTermEnd;

    private Long thirdTermId;
    private String thirdTermName;
    private Double costThirdTerm;
    private Date thirdTermStart;
    private Date thirdTermEnd;

}
