package iuh.fit.trainingsystembackend.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordConfig {
    private boolean requireChangePasswordFirstTime = false;
    private boolean hasChangedPasswordFirstTime = false;
    private String verificationCode;
    private String verificationCodeExpiry = "0";
}
