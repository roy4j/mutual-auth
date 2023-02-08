package com.example.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Value("${server.ssl.client-names}")
    private String[] certClientNames;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests()
                .antMatchers("/server/test").hasRole("CLIENT").and()
                .x509().subjectPrincipalRegex("CN=(.*?),").userDetailsService(userDetailsService()).and()
                .csrf().disable()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (Arrays.asList(certClientNames).contains(username)) {
                return new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_CLIENT"));
            }
            throw new UsernameNotFoundException("Cert is not trusted");
        };
    }
}
