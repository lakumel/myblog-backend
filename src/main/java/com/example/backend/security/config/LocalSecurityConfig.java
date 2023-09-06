package com.example.backend.security.config;

import com.example.backend.domain.enums.Role;
import com.example.backend.security.fitler.ExceptionHandlerFilter;
import com.example.backend.security.fitler.JsonAuthenticationFilter;
import com.example.backend.security.fitler.JwtAuthenticationFilter;
import com.example.backend.security.handler.JsonAuthenticationSuccessHandler;
import com.example.backend.security.provider.JsonAuthenticationProvider;
import com.example.backend.security.service.UserInfoUserDetailsService;
import com.example.backend.security.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Profile("local")
@Order(0)
@Slf4j
public class LocalSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.url.login}")
    private String loginUrl;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final UserInfoUserDetailsService userInfoUserDetailsService;

    private final ExceptionHandlerFilter exceptionHandlerFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(jsonAuthenticationProvider())
                .userDetailsService(userInfoUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//                .requestMatchers(PathRequest.toH2Console());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("excludedUrls : {}", excludedUrls());

        http
                .formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable();

        http
                .csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/logout").permitAll();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/post/new").hasRole(Role.WRITE.name())
                .antMatchers(HttpMethod.PATCH, "/api/post/update/**").hasRole(Role.WRITE.name())
                .antMatchers(HttpMethod.DELETE, "/api/post/delete/**").hasRole(Role.WRITE.name())
                .antMatchers(HttpMethod.GET, "/api/post/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/category/**").permitAll();

        http
                .authorizeRequests()
                .antMatchers("/api/file/upload/**").hasRole(Role.WRITE.name())
                .antMatchers(HttpMethod.GET, "/api/file/**").permitAll()
                .anyRequest().authenticated();

        http
                .logout()
                .logoutUrl("/api/auth/logout");

        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonAuthenticationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, LogoutFilter.class);

        http
                .cors().configurationSource(corsConfigurationSource());

        http
                .exceptionHandling()
                .accessDeniedHandler((request, response, exception) -> {
                    if (response.isCommitted()) {
                        log.info("Did not write to response since already committed");
                        return;
                    }
                    response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
                    log.info("Request Authorization Denied : {} result code : {}", request.getRequestURL(), response.getStatus());
                    return;
                });
    }

    @Bean(name = "excludedUrls")
    @ConfigurationProperties(prefix = "spring.security.exclude.url")
    public List<String> excludedUrls() {
        return new ArrayList<>();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String[] roles = Arrays.stream(Role.values())
                .map(Role::getRole)
                .toArray(String[]::new);

        StringBuilder roleHierarchyString = new StringBuilder();
        for (int i = 0; i < roles.length; i++) {
            if (i != 0) {
                roleHierarchyString.append(">");
            }
            roleHierarchyString.append(roles[i]);
        }

        roleHierarchy.setHierarchy(roleHierarchyString.toString());
        log.info("roleHierarchyString = {}", roleHierarchyString.toString());
        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, excludedUrls());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JsonAuthenticationProvider jsonAuthenticationProvider() {
        return new JsonAuthenticationProvider(passwordEncoder(), userInfoUserDetailsService);
    }

    @Bean
    public JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
        return new JsonAuthenticationSuccessHandler(objectMapper, jwtUtils);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter(loginUrl, objectMapper);
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());

        return jsonAuthenticationFilter;
    }
}
