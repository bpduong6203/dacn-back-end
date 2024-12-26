//package com.example.DoAnCN.Security;
//
//import com.example.DoAnCN.Jwt.JwtFilter;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors(cors -> cors.configurationSource(request -> {
//            CorsConfiguration corsConfiguration = new CorsConfiguration();
//            corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://traveldacn.loca.lt"));
//            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // Cho phép mọi header
//            corsConfiguration.setExposedHeaders(Arrays.asList("Authorization")); // Nếu cần trả header Authorization
//            corsConfiguration.setAllowCredentials(true); // Hỗ trợ cookie hoặc thông tin đăng nhập
//            return corsConfiguration;
//        }))
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/login", "/api/register", "/api/google", "/api/verify-token",
//                                "/api/send-verification-code", "/api/verify-code", "/api/reset-password",
//                                "/api/dest/list", "/api/dest/**", "/uploads/**", "/images/**", "/api/itineraries/**",
//                                "/api/activities/**", "/api/ticketprices/**", "/api/qrcode/**", "/api/bookings/**",
//                                "/api/payments/**", "/api/wish/**", "/api/review/**", "/api/province/**", "/api/city/**",
//                                "/api/payment-methods/**", "/api/banks/**"
//                        ).permitAll()
//
//                        .requestMatchers(HttpMethod.GET, "/api/list-user")
//                        .hasAnyRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.POST, "/api/activities/create", "/api/ticketprices/create" ,
//                                "/api/province/create", "/api/itineraries/create", "/api/dest/create",
//                                "/api/dest/upload-file/{destinationId}", "/api/dest/img", "/api/city/create")
//                        .hasAnyRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT, "/api/activities/{id}", "/api/update-user/{username}",
//                                "/api/{userId}/roles", "/api/ticketprices/{id}", "/api/province/{id}", "/api/payments/{id}/status",
//                                "/api/itineraries/{id}", "/api/dest/update", "/api/city/{id}")
//                        .hasAnyRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.DELETE, "/api/activities/{id}", "/api/itineraries/{id}",
//                                "/api/dest/img/{id}")
//                        .hasAnyRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .rememberMe(rememberMe -> rememberMe
//                        .key("travel")
//                        .rememberMeCookieName("travel")
//                        .tokenValiditySeconds(24 * 60 * 60)
//                        .userDetailsService(userDetailsService)
//                )
//                .exceptionHandling(exception -> exception
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
//                        })
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                        })
//                );
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}







package com.example.DoAnCN.Security;

import com.example.DoAnCN.Jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://traveldacn.loca.lt"));
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // Cho phép mọi header
                    corsConfiguration.setExposedHeaders(Arrays.asList("Authorization")); // Nếu cần trả header Authorization
                    corsConfiguration.setAllowCredentials(true); // Hỗ trợ cookie hoặc thông tin đăng nhập
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/register", "/api/google", "/api/verify-token",
                                "/api/send-verification-code", "/api/verify-code", "/api/reset-password",
                                "/api/dest/list", "/api/dest/**", "/uploads/**", "/images/**", "/api/itineraries/**",
                                "/api/activities/**", "/api/ticketprices/**", "/api/qrcode/**", "/api/bookings/**",
                                "/api/payments/**", "/api/wish/**", "/api/review/**", "/api/province/**", "/api/city/**",
                                "/api/payment-methods/**", "/api/banks/**"
                        ).permitAll()
                        .requestMatchers( "/api/update").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("travel")
                        .rememberMeCookieName("travel")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

