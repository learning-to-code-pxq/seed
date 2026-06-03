package com.tools.seed.common.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class XssFilter extends HttpFilter {

    // 不需要XSS过滤的路径
    private static final Set<String> EXCLUDE_PATHS = Set.of(
            "/auth/login",
            "/auth/register",
            "/auth/password"
    );

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String path = req.getRequestURI();
        if (EXCLUDE_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(req, res);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper(req), res);
        }
    }
}