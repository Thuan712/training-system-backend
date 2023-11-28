package iuh.fit.trainingsystembackend.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAuthCache implements Serializable {
    //#region Session
    private String token;
    private String sessionId;
    // #endregion

    //#region Authentication Attempt
    private int failedLoginAttempts = 0;
    //#endregion

    private Set<String> previousIpAddresses = new HashSet<>(){};
    private String userAgent;
    //#endregion

    //#region Menu Items
    private SystemRole userRole;
    private Long refId;
    //#endregion

}
