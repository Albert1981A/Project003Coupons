package com.AlbertAbuav.Project003Coupons.filters;

import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(2)
public class TokenFilter implements Filter {

    private final TokenManager tokenManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Filter in action!");

        String url = ((HttpServletRequest)servletRequest).getRequestURI();

        if (url.endsWith("login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (url.contains("images")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (url.endsWith("get-coupons")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (url.endsWith("register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = ((HttpServletRequest)servletRequest).getHeader("Authorization");

        if (url.endsWith("logout")) {
            try {
                tokenManager.isExist(token);
            } catch (SecurityException e) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        try {
            tokenManager.isExist(token);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
            ((HttpServletResponse)servletResponse).setStatus(401);
        }
    }
}
