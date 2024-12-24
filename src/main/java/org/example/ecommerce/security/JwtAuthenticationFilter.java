package org.example.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.exceptions.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            // Step 1: Get the Authorization header from the request
            String token = extractToken(request);

            // Step 2: If the token is present, validate it
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // Step 3: Set the authentication in the security context (if token is valid)
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // Step 4: Continue with the request processing
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            String message = e.getMessage();
            LocalDateTime now = LocalDateTime.now();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String output = """
                    {
                        "success": false,
                        "message": "%s",
                        "data": null,
                        "timestamp": "%s"
                    }
                    """.formatted(message, now);

            response.getWriter().write(output);
        }
    }

    // Helper method to extract the token from the Authorization header
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Extract token after "Bearer "
        }
        return null;
    }
}
