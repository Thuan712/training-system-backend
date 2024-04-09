package iuh.fit.trainingsystembackend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDuration {
    // Hệ số thời gian cho thời lượng học

    // Lý thuyết
    private int theory = 0;

    // Thực hành
    private int practice = 0;

    // Tự học
    private int selfLearning = 0;
}
