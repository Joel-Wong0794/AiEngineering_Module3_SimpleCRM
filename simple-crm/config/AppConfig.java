package sg.edu.ntu.simple_crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

public class AppConfig {

    @Configuration
    public class AppConfig {
        @Bean
        public AuthenticationManager authenticaitionManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }
}
