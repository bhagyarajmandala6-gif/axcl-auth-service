package com.innocito.axcl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    //private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/api/masterData").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> {
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        //httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /*@Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain1(HttpSecurity httpSecurity,
                                                    UserDetailsService userDetailsService) throws Exception {
        httpSecurity
                .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**")))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml").authenticated()
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }*/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}