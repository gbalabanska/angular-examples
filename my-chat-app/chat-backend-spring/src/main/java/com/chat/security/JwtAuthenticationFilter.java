//package com.chat.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenUtil jwtTokenUtil;
//
//    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // Remove "Bearer " prefix
//
//            String username = jwtTokenUtil.extractUsername(token);
//            Long userId = jwtTokenUtil.extractUserId(token);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                // Optionally, you can set the user ID in the SecurityContext or request attribute
//                // SecurityContextHolder.getContext().setAuthentication(authentication);
//                request.setAttribute("userId", userId);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//}
