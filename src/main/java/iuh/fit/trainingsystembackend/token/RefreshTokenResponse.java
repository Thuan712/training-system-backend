package iuh.fit.trainingsystembackend.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenResponse {
    private String token;
    private String tokenType = "Bearer";
    private String refreshToken;
    private String expirationDate;
}
