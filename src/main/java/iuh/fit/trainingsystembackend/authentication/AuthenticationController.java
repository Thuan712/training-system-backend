package iuh.fit.trainingsystembackend.authentication;


import iuh.fit.trainingsystembackend.config.JwtToken;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import iuh.fit.trainingsystembackend.token.*;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@RestController
@RequestMapping(Constants.PREFIX_ENDPOINT + "authentication")
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    private UserRepository userEntityRepository;
    private RefreshTokenService refreshTokenService;
    private AuthService authService;
    private UserCacheService userCacheService;

    //#region Issue Token
    @PostMapping(value = "/getToken")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> returnedData = new HashMap<>();
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(), request, response);
        if(response.getStatus() == 409) {
            return ResponseEntity.status(409).body("User account is locked");
        }

        UserEntity userInfo = userEntityRepository.findByUsername(authenticationRequest.getUsername());
        if (userInfo != null) {
            returnedData = authService.generateUserTokenInfo(userInfo, request);
        }

        return ResponseEntity.ok(returnedData);
    }
    //#region

    private void authenticate(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserAuthCache userAuthCache = null;
        UserEntity userEntity = userEntityRepository.findByUsername(username);
        try {
            if(userEntity == null){
                throw new ValidationException("Tài khoản hoặc mật khẩu không đúng !!");
            }
            userAuthCache = userCacheService.getUserAuthCache(username);
        } catch (Exception ex) {
            System.out.println("Fail to get user auth cache");
        }

        if(userAuthCache == null) {
            userAuthCache = new UserAuthCache();
        } else {
            if(userAuthCache.getFailedLoginAttempts() >= 5) {
                response.setStatus(409);
                return;
            }
        }

        if(userEntity == null ){
            throw new ValidationException("Không tìm thấy người dùng !!");
        }

        if(userEntity.isDeleted()) {
            throw new ValidationException("User has been discharged. Please contact your administrator for further access");
        }

        if(!userEntity.isActive()) {
            throw new ValidationException("User has been inactive. Please contact your administrator for further access");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            //update number of fail login attempt
            try {
                userAuthCache.setFailedLoginAttempts(0);
                userCacheService.setUserAuthCache(username, userAuthCache);
            } catch (Exception ex) {
                System.out.println("Fail to update fail login attempt");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
    }

    @PostMapping("/renewToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        if (refreshToken != null && refreshToken.getUser() != null) {
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (refreshToken != null && refreshToken.getUser() != null) {
                final UserDetails userDetails = jwtUserDetailsService
                        .loadUserByUsername(refreshToken.getUser().getUsername());
                String token = jwtToken.generateToken(userDetails);
                RefreshTokenResponse response = new RefreshTokenResponse();
                response.setRefreshToken(requestRefreshToken);
                response.setToken(new JwtResponse(token).getToken());
                response.setExpirationDate(refreshToken.getExpiryDate().toString());

                try {
                    UserAuthCache userAuthCache = userCacheService.getUserAuthCache(refreshToken.getUser().getUsername());
                    if(userAuthCache != null) {
                        userAuthCache.setToken(response.getToken());

                        userCacheService.setUserAuthCache(refreshToken.getUser().getUsername(), userAuthCache);
                    }
                } catch (Exception exception) {
                    System.out.println("Fail to override User Auth Access !");
                }

                return ResponseEntity.ok(response);
            }
        }
        throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
    }

    @PostMapping("/revokeToken")
    public ResponseEntity<?> revokeToken(@RequestParam("refreshToken") String refreshToken) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            FutureTask<String> futureTasks = new FutureTask<>(new Runnable() {
                @Override
                public void run() {
                    try {
                        RefreshToken toDelete = refreshTokenService.findByToken(refreshToken);
                        if (toDelete != null) {
                            refreshTokenService.deleteRefreshToken(toDelete);
                        }
                    } catch (Exception exception) {
                        System.out.println("Fail to revoke token");
                    }
                }
            }, "Done");

            executor.submit(futureTasks);
        } catch (Exception exception) {
            System.out.println("Fail to revoke token");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity getUserAuthInfo(@RequestParam(value = "userId") Long userId, @RequestParam(value = "clientId", required = false) Long clientId, @RequestParam(value = "id") Long id){

//        try {
//            String cacheString = (String) cacheService.loadDataForKey(CacheKeys.USER_DETAIL + id);
//            if (cacheString != null && !cacheString.isEmpty()) {
//                return ResponseEntity.ok(new Gson().fromJson(cacheString, UserInfoDTO.class));
//            }
//        } catch (Exception exception) {
//            System.out.println("Fail to load cache !");
//        }

        UserEntity user = userEntityRepository.findById(id).orElse(null);

        if(user == null){
            throw new ValidationException("User not found !!");
        }

//        UserInfoDTO infoDTO = userInfoMapper.mapToUserAuth(user);
        UserInfoDTO infoDTO = null;

//        try {
//            cacheService.putDataForKey(CacheKeys.USER_DETAIL + id, infoDTO, Duration.ofMinutes(5));
//        } catch (Exception e) {
//            System.out.println("No cache data found");
//        }
        return ResponseEntity.ok(infoDTO);
    }
    //#endregion

//    //#region Forgot Password
//    @PostMapping("/sendVerificationCode")
//    public ResponseEntity sendVerificationCode(
//            @RequestBody Map<String, Object> data,
//            HttpServletRequest request
//    ) {
//        String usernameOrEmail = (String) data.get("usernameOrEmail");
//        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
//            throw new ValidationException("Username or email is required");
//        }
//
//        UserEntity user = userEntityRepository.findUserEntityByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//        if (user == null) {
//            throw new ValidationException("User not found");
//        }
//
//        if (user.getEmail() == null || user.getEmail().isEmpty()) {
//            throw new ValidationException("User does not have email");
//        }
//
//        String verificationCode = StringUtils.randomStringGenerate(6).toUpperCase(Locale.ENGLISH);
//
////        PasswordConfig passwordConfig = user.getPasswordConfig();
////        passwordConfig.setVerificationCode(verificationCode);
////        passwordConfig.setVerificationCodeExpiry(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() + 30 * 60 * 1000)));
////
////        user.setPasswordConfigString(new Gson().toJson(passwordConfig));
//        user = userEntityRepository.saveAndFlush(user);
//        if (user.getId() == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
//        }
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @PostMapping("/checkVerificationCode")
//    public ResponseEntity checkVerificationCode(
//            @RequestBody Map<String, Object> data
//    ) {
//        String verificationCode = (String) data.get("verificationCode");
//        if (verificationCode == null || verificationCode.isEmpty()) {
//            throw new ValidationException("Verification code is required");
//        }
//
//        String usernameOrEmail = (String) data.get("usernameOrEmail");
//        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
//            throw new ValidationException("Username or email is required");
//        }
//
//        UserEntity user = userEntityRepository.findUserEntityByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//        if (user == null) {
//            throw new ValidationException("User not found");
//        }
//
//        if (user.getEmail() == null || user.getEmail().isEmpty()) {
//            throw new ValidationException("User does not have email");
//        }
//
////        PasswordConfig passwordConfig = user.getPasswordConfig();
////        if (!passwordConfig.getVerificationCode().equals(verificationCode)) {
////            throw new ValidationException("Verification code is not correct");
////        }
////
////        if (passwordConfig.getVerificationCodeExpiry() != null && !passwordConfig.getVerificationCodeExpiry().isEmpty()) {
////            try {
////                Date verificationCodeExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(passwordConfig.getVerificationCodeExpiry());
////                if (verificationCodeExpiry.before(new Date())) {
////                    throw new ValidationException("Verification code is expired");
////                }
////            } catch (Exception e) {
////                throw new ValidationException("Verification code is expired");
////            }
////        }
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        String newPassword = (String) data.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ValidationException("New password is required");
        }

        String retypePassword = (String) data.get("retypePassword");
        if (retypePassword == null || retypePassword.isEmpty()) {
            throw new ValidationException("Retype password is required");
        }

        if (!newPassword.equals(retypePassword)) {
            throw new ValidationException("New password and retype password is not match");
        }

//        boolean isValid = userService.checkPasswordValidity(newPassword);
//        if (!isValid) {
//            throw new ValidationException("Password should be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character");
//        }

        String usernameOrEmail = (String) data.get("usernameOrEmail");
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            throw new ValidationException("Username or email is required");
        }

        UserEntity user = userEntityRepository.findUserEntityByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user == null) {
            throw new ValidationException("User not found");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("User does not have email");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encodedPassword);

//        PasswordConfig passwordConfig = user.getPasswordConfig();
//        passwordConfig.setVerificationCode(null);
//        passwordConfig.setVerificationCodeExpiry(null);
//        user.setPasswordConfigString(new Gson().toJson(passwordConfig));

        user = userEntityRepository.saveAndFlush(user);
        if (user.getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password");
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }



}
