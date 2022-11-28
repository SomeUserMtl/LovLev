package com.project.lovlev.configs;

import com.project.lovlev.services.JpaUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JpaUserDetailService jpaUserDetailService;

    public SecurityConfig(JpaUserDetailService jpaUserDetailService) {
        this.jpaUserDetailService = jpaUserDetailService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .antMatchers(HttpMethod.POST, "user**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(jpaUserDetailService)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("passwordEncoder: " + bCryptPasswordEncoder);
        return new BCryptPasswordEncoder();
    }
}
