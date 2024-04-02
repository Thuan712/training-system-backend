package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TuitionRequest {
    private Double initialFee;

    private Double discountAmount;

    private Double plusDeductions;

    private Double minusDeductions;

    private String otherInformation;
}
