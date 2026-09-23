package sg.edu.ntu.simple_crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception

        {

                http
                                // Disable CSRF because this REST API uses HTTP Basic instead of browser
                                // sessions.
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                // All configured roles may read customer data.
                                                .requestMatchers(HttpMethod.GET, "/customers/**")
                                                .hasAnyRole("USER", "MANAGER", "ADMIN")
                                                // Only administrators may create new customers.
                                                .requestMatchers(HttpMethod.POST, "/customers/**").hasRole("ADMIN")
                                                // Managers and administrators may update existing customers.
                                                .requestMatchers(HttpMethod.PUT, "/customers/**")
                                                .hasAnyRole("MANAGER", "ADMIN")
                                                // Only administrators may delete customers.
                                                .requestMatchers(HttpMethod.DELETE, "/customers/**").hasRole("ADMIN")
                                                // Require authentication for every other endpoint.
                                                .anyRequest().authenticated())
                                // Authenticate requests using credentials in the HTTP Basic header.
                                .httpBasic(Customizer.withDefaults());

                return http.build();

        }

        // Use BCrypt to hash and verify account passwords.
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Register the application accounts in memory.
        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
                // Standard account with read-only customer access.
                UserDetails user = User.builder()
                                .username("user")
                                .password(passwordEncoder.encode("password"))
                                .roles("USER")
                                .build();

                // Manager account with read and update access.
                UserDetails manager = User.builder()
                                .username("manager")
                                .password(passwordEncoder.encode("manager123"))
                                .roles("MANAGER")
                                .build();

                // Administrator account with full customer access.
                UserDetails admin = User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin123"))
                                .roles("ADMIN")
                                .build();

                // Expose all three accounts to Spring Security.
                return new InMemoryUserDetailsManager(user, manager, admin);

        }

}
