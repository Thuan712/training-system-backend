package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.CourseType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionBean implements Serializable {
    //Section
    private Long id;
    private Long termId;
    private Long courseId;
    private String name;
    private String description;
    private Boolean deleted;
    private Date openDate;
    private Date lockDate;

    // Tuition
    private Double initialFee;
    private Double discountAmount = 0D;
    private Double discountFee = 0D;
    private Date paymentDeadline;
}
