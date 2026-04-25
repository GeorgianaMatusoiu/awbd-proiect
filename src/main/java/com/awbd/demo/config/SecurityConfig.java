package com.awbd.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public UserDetailsService userDetailsService(JdbcUserDetailsManager jdbcUserDetailsManager) {
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices services =
                new TokenBasedRememberMeServices("awbd-remember-me-key", userDetailsService);
        services.setCookieName("remember-me");
        services.setParameter("rememberMe");
        services.setTokenValiditySeconds(7 * 24 * 60 * 60);
        services.setAlwaysRemember(true);
        return services;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenBasedRememberMeServices rememberMeServices,
                                                   DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .authenticationProvider(authenticationProvider)
                .rememberMe(remember -> remember
                        .rememberMeServices(rememberMeServices)
                        .key("awbd-remember-me-key")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/auth/register", "/auth/login", "/auth/logout", "/auth/me").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/clienti").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/prospecte").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medicamente").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/retete").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/carduri").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/profiluri").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/detalii-retete").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/clienti/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prospecte/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicamente/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/retete/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/carduri/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profiluri/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/detalii-retete/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/clienti/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prospecte/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicamente/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/retete/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/carduri/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/profiluri/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/detalii-retete/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/farmacisti").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/furnizori").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categorii").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/farmacisti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/furnizori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categorii/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/farmacisti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/furnizori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorii/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}