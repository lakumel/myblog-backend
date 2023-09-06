package com.example.backend.security.config;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
//@Configuration
//@RequiredArgsConstructor
public class JsonSecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//    @Value("${security.url.login}")
//    private String loginUrl;
//    private final ObjectMapper objectMapper;
//
//    private final UserInfoRepository userInfoRepository;
//
////    private final UserInfoService userInfoService;
//
//    private final JwtUtils jwtUtils;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .authenticationProvider(jsonAuthenticationProvider());
////                .userDetailsService(userInfoService())
////                .userDetailsService(userInfoService)
////                .passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .headers().frameOptions().disable();
//
//        http
//                .addFilterBefore(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .formLogin().disable()
//                .httpBasic().disable()
//                .cors().configurationSource(corsConfigurationSource());
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("*");
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//        configuration.setAllowCredentials(true);
//        configuration.setExposedHeaders(Arrays.asList("Authorization"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public UserInfoService userInfoService(){
////        return new UserInfoService(passwordEncoder(), userInfoRepository);
////    }
//
//
//    @Bean
//    public JsonAuthenticationProvider jsonAuthenticationProvider() {
//        return new JsonAuthenticationProvider(passwordEncoder(), userDetailsService());
//    }
//
//    @Bean
//    public JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
//        return new JsonAuthenticationSuccessHandler(objectMapper, jwtUtils);
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
//        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter(loginUrl, objectMapper);
//        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
//        jsonAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
//
//        return jsonAuthenticationFilter;
//    }
}
