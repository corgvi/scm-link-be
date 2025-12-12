package com.cvv.scm_link.configuration;

import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cvv.scm_link.entity.Role;
import com.cvv.scm_link.entity.User;
import com.cvv.scm_link.repository.RoleRepository;
import com.cvv.scm_link.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    static String ADMIN_USERNAME = "admin";
    static String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            log.info("Init Application");
            if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
                roleRepository.save(Role.builder()
                        .name(com.cvv.scm_link.enums.Role.USER.name())
                        .build());
                Role adminRole = roleRepository.save(Role.builder()
                        .name(com.cvv.scm_link.enums.Role.ADMIN.name())
                        .build());
                User user = User.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .isActive(true)
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin.");
            }
        };
    }
}
