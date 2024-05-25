package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramBean implements Serializable {
    private Long id;
    private Long specializationId;
    private Long academicYearId;

    private String name;
    private Double trainingTime;
    private List<ProgramTermBean> programTerms = new ArrayList<>();
}
