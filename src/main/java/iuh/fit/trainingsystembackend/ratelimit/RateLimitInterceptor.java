package iuh.fit.trainingsystembackend.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor  implements HandlerInterceptor {
    private static final String HEADER_API_KEY = "X-Api-Key";
    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

    @Autowired
    private PricingService pricingPlanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String mode = System.getProperty("mode");
        if(mode != null && mode.equals("dev"))
            return true;

        String apiKey = request.getHeader(HEADER_API_KEY);

        if (apiKey == null || apiKey.isEmpty()) {
            throw new ValidationException("Missing Header: " + HEADER_API_KEY);
        }

        if(apiKey.equals("UNLIMITED"))
        	return true;

        Bucket tokenBucket = pricingPlanService.resolveBucket(apiKey);

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            return true;

        } else {

            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.addHeader(HEADER_RETRY_AFTER, String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota"); // 429

            return false;
        }
    }
}
