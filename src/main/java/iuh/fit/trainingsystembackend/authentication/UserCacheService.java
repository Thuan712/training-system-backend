package com.thinkvitals.authentication;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thinkvitals.cache.CacheKeys;
import com.thinkvitals.cache.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCacheService {
    private CacheService cacheService;

    public UserAuthCache getUserAuthCache(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }

        UserAuthCache userCache = null;

        try {
            String existingCachedDataString = (String) cacheService.loadDataForKey(CacheKeys.AUTH_ACCESS + "-" + username);
            if(existingCachedDataString == null || existingCachedDataString.isEmpty()) {
                return null;
            }

            userCache = new Gson().fromJson(existingCachedDataString, new TypeToken<UserAuthCache>() {}.getType());
        } catch (Exception exception) {
            System.out.println("Fail to get user data from cache !!");
        }

        return userCache;
    }

    public void setUserAuthCache(String username, UserAuthCache toCachedData) {
        if(username != null && !username.isEmpty() && toCachedData != null) {
            try {
                cacheService.putPermanentDataForKey(CacheKeys.AUTH_ACCESS + "-" + username, new Gson().toJson(toCachedData));
            } catch (Exception ignored) {}
        }
    }
}
