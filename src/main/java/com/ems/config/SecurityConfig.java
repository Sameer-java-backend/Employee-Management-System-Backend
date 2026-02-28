package com.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ems.security.JWTAuthFilter;
import com.ems.services.OurUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**", "/public/**").permitAll() // 1. Login/Register sabke liye khula hai
                        .requestMatchers(HttpMethod.POST,"/api/departments").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/departments").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/departments/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN") // 2. Admin URL sirf ADMIN ke liye
                        .requestMatchers("/user/**").hasAnyAuthority("USER")   // 3. User URL sirf USER ke liye
                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "USER") // 4. Dono ke liye
                        .anyRequest().authenticated()) // 5. Baaki sab lock kar do
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 6. Session mat banao
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // 7. Hamara Filter pehle lagao

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(ourUserDetailsService);
       daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
       
       return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

