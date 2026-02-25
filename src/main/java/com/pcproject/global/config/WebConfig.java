package com.pcproject.global.config;

import com.pcproject.global.interceptor.AdminAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthInterceptor)
                // 주문 생성
                .addPathPatterns("/api/orders")
                // 상태 변경
                .addPathPatterns("/api/orders/*/status")
                // 주문 취소
                .addPathPatterns("/api/orders/*/cancel");
    }
}
