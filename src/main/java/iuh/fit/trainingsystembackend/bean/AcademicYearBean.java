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
    private String firstTermStart;
    private String firstTermEnd;

    private Long secondTermId;
    private String secondTermName;
    private String secondTermStart;
    private String secondTermEnd;

    private Long thirdTermId;
    private String thirdTermName;
    private String thirdTermStart;
    private String thirdTermEnd;

}
