package iuh.fit.trainingsystembackend.telephone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SMS {
    private List<String> phoneNumbers;
    private String message;
    private String callMessage;
    private boolean useCustomPhone = false;
    private String customPhone;
    private String accountSID;
    private String authToken;
}
