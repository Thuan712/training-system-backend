package iuh.fit.trainingsystembackend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ward {
    private String code;
    private String name;
    private String nameEN;
    private String fullName;
    private String fullNameEN;
    private String districtCode;
    private int administrativeUnitId;
}
