package com.mohity.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = org.springframework.security.core.userdetails.User.withUsername("admin")
                .password(passwordEncoder().encode("admin_pass"))
                .roles("ADMIN")
                .build();

        UserDetails member = User.withUsername("member")
                .password(passwordEncoder().encode("member_pass"))
                .roles("MEMBER")
                .build();

        return new InMemoryUserDetailsManager(admin, member);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasAnyRole("MEMBER", "ADMIN")
                        .requestMatchers("/api/users/signup").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());

        return httpSecurity.build();
    }
}
