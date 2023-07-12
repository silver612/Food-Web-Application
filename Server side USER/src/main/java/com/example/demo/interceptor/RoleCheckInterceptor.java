package com.example.demo.interceptor;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.data.enums.Role;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor{
    
    @Override
    public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response, 
    Object handler) throws Exception {
        
        if(request.getRequestURL().indexOf("/secure")==-1)
            return true;
        if(request.getMethod().equals("OPTIONS"))
            return true;

        Cookie[] cookies = request.getCookies();

        Optional<Cookie> roleMaybe = Arrays.stream(cookies).filter(c -> "role".equals(c.getName())).findFirst();

        Role role = Role.valueOf(roleMaybe.get().getValue());

        if((request.getRequestURL().indexOf("/secure1")!=-1 && role!=Role.USER) 
        || (request.getRequestURL().indexOf("/secure2")!=-1 && role!=Role.VENDOR))
        {
            response.getWriter().write("Unauthorized Access");
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
