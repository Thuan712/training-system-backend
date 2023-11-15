package iuh.fit.trainingsystembackend.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenRequest {
    @NonNull
    private String refreshToken;
}
