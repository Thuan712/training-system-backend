package iuh.fit.trainingsystembackend;

import iuh.fit.trainingsystembackend.cache.CacheService;
import iuh.fit.trainingsystembackend.ratelimit.RateLimitInterceptor;
import iuh.fit.trainingsystembackend.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
@EnableHystrix
public class TrainingSystemBackendApplication implements WebMvcConfigurer {
    @Autowired
    @Lazy
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/backend/api/**");
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(TrainingSystemBackendApplication.class, args);
        System.setProperty("mode", Constants.rateLimitMode);
    }
}
