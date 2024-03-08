package com.muud.global.config;

//import com.muud.auth.jwt.JwtInterceptor;
import com.muud.auth.jwt.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Value("${spring.cors.origin.local}")
    private String local;

    @Value("${spring.cors.origin.server}")
    private String server;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins(local, server)
                .allowedMethods("*")
                .allowCredentials(true);
    }

//    private String[] excludePathPatterns = {
//            "/auth/**", "/auth/kakao/signin"
//    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor);
//                .addPathPatterns("/**")
//                .excludePathPatterns(excludePathPatterns);
    }
}
