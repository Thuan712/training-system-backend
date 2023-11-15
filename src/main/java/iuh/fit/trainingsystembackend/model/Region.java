package iuh.fit.trainingsystembackend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {
    private int id;
    private String name;
    private String nameEN;
    private String codeName;
    private String codeNameEN;
}
