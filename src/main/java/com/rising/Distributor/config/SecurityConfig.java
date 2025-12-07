package com.rising.Distributor.config;
import com.rising.Distributor.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String apiProducts = "/api/products";
        String apiAuth = "/api/auth/";
        String apiUsers = "/api/users/";

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public Authentication Endpoints
                        .requestMatchers(apiAuth + "register/**").permitAll()
                        .requestMatchers(apiAuth + "login").permitAll()
                        .requestMatchers(apiAuth + "refresh").permitAll()
                        
                        // Public User Management Endpoints
                        .requestMatchers(apiUsers + "password/request-reset").permitAll()
                        .requestMatchers(apiUsers + "password/reset").permitAll()
                        
                        // Public Product Viewing
                        .requestMatchers(HttpMethod.GET, apiProducts + "/**").permitAll()
                        
                        // Admin Product Management
                        .requestMatchers(HttpMethod.POST, apiProducts).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, apiProducts + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, apiProducts + "/**").hasRole("ADMIN")
                        
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
