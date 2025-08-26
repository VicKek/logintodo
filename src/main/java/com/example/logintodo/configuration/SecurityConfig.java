package com.example.logintodo.configuration;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final PersonRepository personRepository;

    public SecurityConfig(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/h2-console/**",  // allow H2 console
                                "/css/**",         // static CSS
                                "/js/**"           // static JS
                        ).permitAll()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // Login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // Redirect to the user's assignments after login
                            Person user = personRepository.findByUserName(authentication.getName())
                                    .orElseThrow(() -> new RuntimeException("User not found"));
                            response.sendRedirect("/assignments/" + user.getId());
                        })
                )
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Store a message in session (for display on login page)
                            request.getSession().setAttribute("logoutMessage", "You have successfully logged out.");
                            response.sendRedirect("/login");
                        })
                )
                // CSRF configuration
                .csrf(csrf -> csrf
                        // Disable CSRF for H2 console to allow its POST requests
                        .ignoringRequestMatchers("/h2-console/**")
                )
                // Headers configuration for H2 console frames
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}