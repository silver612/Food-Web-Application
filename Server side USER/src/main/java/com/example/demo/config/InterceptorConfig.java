package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.CorsHeaderAdderInterceptor;
import com.example.demo.interceptor.LoggedInCheckerInterceptor;
import com.example.demo.interceptor.RoleCheckInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Autowired
    private LoggedInCheckerInterceptor loggedCheckerInterceptor;

    @Autowired
    private CorsHeaderAdderInterceptor corsHeaderAdderInterceptor;

    @Autowired
    private RoleCheckInterceptor roleCheckInterceptor;

    @Override
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsHeaderAdderInterceptor);
		registry.addInterceptor(loggedCheckerInterceptor);
        registry.addInterceptor(roleCheckInterceptor);
	}
}
