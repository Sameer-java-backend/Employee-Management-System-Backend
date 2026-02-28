package com.ems.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ems.services.OurUserDetailsService;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request se "Authorization" header nikala
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // 2. Check kiya ki Header khali toh nahi hai aur "Bearer " se shuru ho raha hai?
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); 
            return;
        }

        // 3. "Bearer " hata kar sirf Token nikala (7 characters hata diye)
        jwtToken = authHeader.substring(7);

        // 4. Token se Email (Username) nikala
        userEmail = jwtUtils.extractUsername(jwtToken);

        // 5. Agar Email mil gaya aur banda pehle se authenticated nahi hai
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Database se User load karo
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

            // 6. Token valid hai ya nahi check kiya
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                
                // Agar valid hai, toh ek "Authentication Token" banao
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Final Step: Spring Security ko bata do ki "Yeh banda Verified hai!"
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        
        // 8. Request ko aage jaane do (Controller ki taraf)
        filterChain.doFilter(request, response);
    }
}