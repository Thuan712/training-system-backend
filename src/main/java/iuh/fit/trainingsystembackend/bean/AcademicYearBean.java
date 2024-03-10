package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcademicYearBean {
    private Long id;
    private String name;
    private boolean active = true;

    private Long firstTermId;
    private String firstTermName;
    private Date firstTermStart;
    private Date firstTermEnd;

    private Long secondTermId;
    private String secondTermName;
    private Date secondTermStart;
    private Date secondTermEnd;

    private Long thirdTermId;
    private String thirdTermName;
    private Date thirdTermStart;
    private Date thirdTermEnd;

}
