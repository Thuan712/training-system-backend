package com.thinkvitals.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thinkvitals.enums.SystemRole;
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

    //#region Access Location
    private String ipAddress;
    private Set<String> previousIpAddresses = new HashSet<>(){};
    private String userAgent;
    //#endregion

    //#region Menu Items
    private SystemRole userRole;
    private String menuItems;
    //#endregion

    //#region Hospital Logo
    private Long hospitalId;
    private String hospitalLogo;
    private String hospitalName;
    //#endregion

    //#region Notification Setting
    private String notificationSetting;
    //#endregion

    //#region Hospitals Belong
    private String hospitalsBelong;
    //#endregion

}
