package com.chat.security;

import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userDetailsService;

    @Autowired
    private CookieExtractor cookieExtractor;

    private static final int TOKEN_EXPIRATION_THRESHOLD_MINUTES = 5;
    private static final long MILLISECONDS_IN_A_MINUTE = 60 * 1000;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("==================== Request URI: " + requestURI + " ====================");

        // Handle OPTIONS request for CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            handlePreflightRequest(response);
            return;
        }

        String token = null;
        String username = null;

        // Look for the "token" cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    username = jwtService.extractUsername(token);  // Extract username from the token
                    System.out.println("==================== Request came from user: " + username + " ====================");
                    break;
                }
            }
        }

        // If a token is found and no authentication is set in the context
        if (token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token and set authentication if valid
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Check if the token is close to expiration (less than 5 minutes remaining)
                long expirationTime = jwtService.extractExpiration(token).getTime();
                long currentTime = new Date().getTime();
                long remainingTimeInMillis = expirationTime - currentTime;
                long remainingTimeInMinutes = remainingTimeInMillis / MILLISECONDS_IN_A_MINUTE;
                System.out.println("token remainingTimeInMinutes=" + remainingTimeInMinutes);

                // If the token is expiring in less than 5 minutes, generate a new one and set it in the cookie
                if (remainingTimeInMinutes < TOKEN_EXPIRATION_THRESHOLD_MINUTES) {
                    String newToken = jwtService.generateToken(username);
                    Cookie newTokenCookie = new Cookie("token", newToken);
                    newTokenCookie.setHttpOnly(true);
                    newTokenCookie.setSecure(true);  // Set this flag if using HTTPS
                    newTokenCookie.setPath("/");  // Make the cookie available for the entire domain
                    response.addCookie(newTokenCookie);  // Add the new token cookie to the response

                    System.out.println("==================== Token refreshed and added to the response ====================");
                }
            }
        }

        System.out.println("==================== Continue the filter chain ====================");

        // Continue the filter chain for non-OPTIONS requests
        filterChain.doFilter(request, response);
    }

    private void handlePreflightRequest(HttpServletResponse response) {
        System.out.println("==================== OPTIONS REQUEST ====================");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "https://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache preflight request for 1 hour
    }
}
