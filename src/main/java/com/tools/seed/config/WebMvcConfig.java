package com.tools.seed.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",       // 登录接口放行
                        "/auth/register",    // 注册接口放行
                        "/test/**",          // 测试接口放行
                        "/error",            // Spring默认错误页
                        "/doc.html",         // 后续springdoc文档页
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                );
    }
}
