package com.metacoding.restserver._core.config;

import com.metacoding.restserver._core.filter.CorsFilter;
import com.metacoding.restserver._core.filter.JwtAuthorizationFilter;
import com.metacoding.restserver._core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final JwtUtil jwtUtil;

    // 인가 필터 => 401 뜨면 이 필터 통과 못 한 것
    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean = new FilterRegistrationBean<>(new JwtAuthorizationFilter(jwtUtil));
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1); // 낮은 번호부터 실행됨 => 이 경우 두번째 실행
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter());
        bean.addUrlPatterns("/*"); // * 하나만 써야됨
        bean.setOrder(0); // 낮은 번호부터 실행됨
        return bean;
    }
}