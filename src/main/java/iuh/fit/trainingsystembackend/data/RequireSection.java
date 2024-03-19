package iuh.fit.trainingsystembackend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequireSection {
    private List<String> studyFirst = new ArrayList<>();
    private List<String> parallel = new ArrayList<>();
    private List<String> prerequisite = new ArrayList<>();
}
