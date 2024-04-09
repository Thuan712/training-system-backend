package iuh.fit.trainingsystembackend.authentication;


import iuh.fit.trainingsystembackend.config.JwtToken;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.StudentRepository;
import iuh.fit.trainingsystembackend.token.JwtUserDetailsService;
import iuh.fit.trainingsystembackend.token.RefreshToken;
import iuh.fit.trainingsystembackend.token.RefreshTokenResponse;
import iuh.fit.trainingsystembackend.token.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    private RefreshTokenService refreshTokenService;
private StudentRepository studentRepository;
private LecturerRepository lecturerRepository;
    private UserCacheService userCacheService;


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
                existingCachedData.setUserAgent(userAgent);

                existingCachedData.setPreviousIpAddresses(previousIpAddresses);

                if (userEntity.getSystemRole().equals(SystemRole.admin)) {
                    existingCachedData.setUserRole(SystemRole.admin.getValue());
                } else if (userEntity.getSystemRole().equals(SystemRole.lecturer)) {
                    existingCachedData.setUserRole(SystemRole.lecturer.getValue());

                    Lecturer lecturer = lecturerRepository.getLecturersByUserId(userEntity.getId());

                    if(lecturer != null){
                        existingCachedData.setRefId(lecturer.getId());
                    }

                } else if (userEntity.getSystemRole().equals(SystemRole.student)) {
                    existingCachedData.setUserRole(SystemRole.student.getValue());

                    Student student = studentRepository.getStudentByUserId(userEntity.getId());

                    if(student != null){
                        existingCachedData.setRefId(student.getId());
                    }
                }

                userCacheService.setUserAuthCache(userEntity.getUsername(), existingCachedData);

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
            if (userEntity.getSystemRole().equals(SystemRole.admin)) {
                returnedData.put("userRole", "Admin");
            } else if (userEntity.getSystemRole().equals(SystemRole.lecturer)) {
                returnedData.put("userRole", "Lecturer");

                Lecturer lecturer = lecturerRepository.getLecturersByUserId(userEntity.getId());

                if(lecturer != null){
                    returnedData.put("refId", lecturer.getId());
                }
            } else if (userEntity.getSystemRole().equals(SystemRole.student)) {
                returnedData.put("userRole", "Student");

                Student student = studentRepository.getStudentByUserId(userEntity.getId());

                if(student != null){
                    returnedData.put("refId", student.getId());
                }
            }
            //#endregion
        }

        return returnedData;
    }
}
