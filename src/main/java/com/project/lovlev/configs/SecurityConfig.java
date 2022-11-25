package com.project.lovlev.configs;

import com.project.lovlev.services.JpaUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.swing.text.html.Option;
import java.util.Optional;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    private final JpaUserDetailService jpaUserDetailService;
//
    public SecurityConfig(JpaUserDetailService jpaUserDetailService) {
        this.jpaUserDetailService = jpaUserDetailService;
    }

    //Hardcoded for development purposes

//    @Bean
//    public InMemoryUserDetailsManager user(){
//        System.out.println(passwordEncoder().encode("user"));
//        return new InMemoryUserDetailsManager(
//                User.withUsername("user")
//                        .password(passwordEncoder().encode("user"))
//                        .authorities("read")
//                        .build()
//        );
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .antMatchers("/admin/**").hasRole("ADMIN")
                                .antMatchers("/user/**").hasRole("USER")
                                .antMatchers("/register/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(jpaUserDetailService)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
//        return http
//                .csrf().disable()
//                .authorizeRequests((authorize) -> authorize
////                        .antMatchers(HttpMethod.GET,"/admin/users/**").authenticated()
//                        .anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
////                .userDetailsService(jpaUserDetailService)
//                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("passwordEncoder: " + bCryptPasswordEncoder);
        return new BCryptPasswordEncoder();
    }

    // Get principal
    @Bean
    public Optional<Authentication> getAuthentication() {
        System.out.println("getAuthentication: " + SecurityContextHolder.getContext().getAuthentication());
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}
