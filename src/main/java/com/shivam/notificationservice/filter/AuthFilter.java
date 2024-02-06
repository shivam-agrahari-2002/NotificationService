package com.shivam.notificationservice.filter;


import com.shivam.notificationservice.constants.Constants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        log.info(req.getMethod() + " : " +req.getRequestURL());
        String key = req.getHeader(Constants.INTERNAL_API_AUTH_HEADER);
        if (key != null && key.equals(Constants.INTERNAL_API_KEY)) {
            // Key is correct, proceed with the filter chain
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // Key is incorrect, send "Not Authorized" response
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            String errorMessage = "{\"error\":\"Not Authorized\",\"comment\":\"wrong API key\"}";
            res.getWriter().write(errorMessage);
        }

    }
}
