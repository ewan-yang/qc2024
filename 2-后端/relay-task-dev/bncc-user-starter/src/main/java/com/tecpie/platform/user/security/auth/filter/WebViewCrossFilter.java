package com.tecpie.platform.user.security.auth.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

@Component
public class WebViewCrossFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaders("Access-Control-Request-Headers");
        if(Objects.nonNull(headerNames)) {
            while (headerNames.hasMoreElements()) {
                headers.append(headerNames.nextElement()).append(",");
            }
        }
        response.setHeader("Access-Control-Allow-Headers", headers.toString());

        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
