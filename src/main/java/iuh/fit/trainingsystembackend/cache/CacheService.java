package iuh.fit.trainingsystembackend.cache;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@AllArgsConstructor
public class CacheService {
    private static String KEY_PREFIX = "LuxuryMall";
    private RedisStoreRepository redisStoreRepository;

    public String getKey(String key) {
        return KEY_PREFIX + "-" + key;
    }

    public Set<String> getAllKeysByPattern(String pattern) {
        //e.g CacheKeys.AUTH_ACCESS + "-" + clientId + "-"
        String keyPattern = getKey(pattern) + "*";
        return redisStoreRepository.getAllKeys(keyPattern);
    }

    public void putDataForKey(String key, Object data, Duration duration) {
        String storedKey = getKey(key);
        redisStoreRepository.save(storedKey, data, duration);
    }

    public void putPermanentDataForKey(String key, Object data) {
        String storedKey = getKey(key);
        redisStoreRepository.save(storedKey, data);
    }

    public Object loadDataForKey(String key) {
        return redisStoreRepository.get(getKey(key));
    }

    public void evictDataForKey(String key) {
        redisStoreRepository.remove(key);
    }

    public void clearWithPattern(String pattern) {
        String keyPattern = getKey(pattern) + "*";
        Set<String> keys = redisStoreRepository.getAllKeys(keyPattern);
        if(!keys.isEmpty()) {
            for (String key : keys) {
                evictDataForKey(key);
            }
        }
    }

    public void clearAll() {
        String keyPattern = getKey("*");
        Set<String> keys = redisStoreRepository.getAllKeys(keyPattern);
        if(!keys.isEmpty()) {
            for (String key : keys) {
                evictDataForKey(key);
            }
        }
    }
}
