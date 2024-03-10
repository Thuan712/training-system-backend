package iuh.fit.trainingsystembackend.bean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacultyBean {
    private Long id;

    private String name;

    private String code;

    private String logo;

    private String headName;

    private String headEmail;

    private String headPhone;

    private Date establishmentDate = new Date();
}
