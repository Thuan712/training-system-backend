package com.thinkvitals.token;

import com.thinkvitals.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;

    public static final long JWT_TOKEN_VALIDITY = 90 * 24 * 60 * 60;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public RefreshToken createRefreshToken(UserEntity userEntity) {
        if(userEntity == null) {
            throw new TokenRefreshException(null, "User id cannot be null");
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userEntity);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(JWT_TOKEN_VALIDITY * 1000));

        refreshToken = refreshTokenRepository.saveAndFlush(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);

            throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return refreshToken;
    }
}
