package com.thinkvitals.authentication;

import com.maxmind.geoip2.model.CityResponse;
import com.thinkvitals.config.JwtToken;
import com.thinkvitals.enums.SystemRole;
import com.thinkvitals.exceptions.ValidationException;
import com.thinkvitals.mail.MailEnvelope;
import com.thinkvitals.mail.MailTemplates;
import com.thinkvitals.model.UserEntity;
import com.thinkvitals.rabbitmq.RabbitMQSender;
import com.thinkvitals.service.IPService;
import com.thinkvitals.token.JwtUserDetailsService;
import com.thinkvitals.token.RefreshToken;
import com.thinkvitals.token.RefreshTokenResponse;
import com.thinkvitals.token.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    private RefreshTokenService refreshTokenService;

    private UserCacheService userCacheService;
    private IPService ipService;
    private RabbitMQSender rabbitMQSender;

    public Map<String, Object>  generateUserTokenInfo(UserEntity userEntity, HttpServletRequest request) throws Exception {
        if (userEntity == null || userEntity.getUsername() == null || userEntity.getUsername().isEmpty()) {
            throw new ValidationException("Username is required !");
        }

        Map<String, Object> returnedData = new HashMap<>();

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userEntity.getUsername());

        final String token = jwtToken.generateToken(userDetails);
        if (token != null && !token.isEmpty()) {
            String sessionId = UUID.randomUUID().toString();

            try {
                // Get user location
                String ipAddress = ipService.getPublicIpAddress();
                CityResponse cityResponse = ipService.getLocationFromIP(ipAddress);

                // Get user agent
                String userAgent = request.getHeader("User-Agent");

                // Store User Auth Cache
                Set<String> previousIpAddresses = new HashSet<>();
                UserAuthCache existingCachedData = userCacheService.getUserAuthCache(userEntity.getUsername());
                if (existingCachedData == null) {
                    existingCachedData = new UserAuthCache();
                }

                existingCachedData.setToken(token);
                existingCachedData.setSessionId(sessionId);
                existingCachedData.setIpAddress(ipAddress);
                existingCachedData.setUserAgent(userAgent);

                previousIpAddresses.add(ipAddress);
                existingCachedData.setPreviousIpAddresses(previousIpAddresses);

                if (userEntity.getSystemRole().equals(SystemRole.superadmin)) {
                    existingCachedData.setMenuItems("all");
                    existingCachedData.setUserRole(SystemRole.superadmin);
                } else if (userEntity.getSystemRole().equals(SystemRole.admin)) {
                    existingCachedData.setMenuItems("all_except_client");
                    existingCachedData.setUserRole(SystemRole.admin);
                } else if (userEntity.getSystemRole().equals(SystemRole.customer)) {
                    existingCachedData.setMenuItems("all_except_client");
                    existingCachedData.setUserRole(SystemRole.customer);
                }

                userCacheService.setUserAuthCache(userEntity.getUsername(), existingCachedData);
                // Execute in a separate thread
                ExecutorService executor = Executors.newFixedThreadPool(1);
                UserAuthCache finalExistingCachedData = existingCachedData;
                FutureTask<String> futureTasks = new FutureTask<>(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (finalExistingCachedData.getIpAddress() != null && !finalExistingCachedData.getIpAddress().equals(ipAddress) && !previousIpAddresses.contains(ipAddress) && userEntity.getEmail() != null && !userEntity.getEmail().isEmpty()) {
                                // send an alert email to user if there is any location difference
                                MailEnvelope mailEnvelop = new MailEnvelope();
                                mailEnvelop.setTo(userEntity.getEmail());
                                mailEnvelop.setSubject("Luxury Mall - Sign-in Attempt from Different Location");
                                mailEnvelop.setTemplate(MailTemplates.AUTHENTICATION_SECURITY_ALERT);
                                mailEnvelop.setData(new HashMap<String, Object>() {{
                                    put("fullName", userEntity.getFullName());
                                    put("ipAddress", ipAddress);
                                    put("userAgent", userAgent);
                                    put("origin", cityResponse != null && cityResponse.getCity() != null ? cityResponse.getCity().getName() + ", " + cityResponse.getCountry().getName() : "Unknown");
                                }});

//                                rabbitMQSender.sendMail(mailEnvelop);
                            }
                        } catch (Exception e) {
                            System.out.println("Fail to store token");
                        }

                    }
                }, "Future Tasks Completed !!");

                executor.submit(futureTasks);
            } catch (Exception e) {
                System.out.println("Fail to execute future tasks");
            }

            //#region User Info
            returnedData.put("userInfo", userEntity);
            //#endregion 

            //#region Refresh Token
            RefreshTokenResponse response = new RefreshTokenResponse();
            response.setToken(new JwtResponse(token).getToken());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity);
            response.setRefreshToken(refreshToken.getToken());
            response.setExpirationDate(refreshToken.getExpiryDate().toString());
            returnedData.put("userToken", response);
            //#endregion

            //#region X-API-KEY
            returnedData.put("xApiKey", "BX001-");
            //#endregion

            //#region Session
            returnedData.put("sessionId", sessionId);
            //#endregion

            //#region Menu Items
            if (userEntity.getSystemRole().equals(SystemRole.superadmin)) {
                returnedData.put("menuItems", "all");
                returnedData.put("userRole", "Super Admin");
            } else if (userEntity.getSystemRole().equals(SystemRole.admin)) {
                returnedData.put("menuItems", "all_except_client");
                returnedData.put("userRole", "Admin");
            } else {
                // TODO
            }
            //#endregion

            //#endregion


        }

        return returnedData;
    }
}
