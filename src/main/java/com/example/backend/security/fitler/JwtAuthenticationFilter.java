package com.example.backend.security.fitler;

import com.example.backend.advice.dto.ExceptionResponse;
import com.example.backend.security.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final SkipJwtAuthenticationMatcher skipJwtAuthenticationMatcher;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, List<String> excludedUrls){
        this.jwtUtils = jwtUtils;
        skipJwtAuthenticationMatcher = new SkipJwtAuthenticationMatcher(excludedUrls);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(!skipJwtAuthenticationMatcher.matches(request) &&
                !PathRequest.toStaticResources().atCommonLocations().matches(request)) {
            String token = "";
            String tokenHeader = request.getHeader("Authorization");

            if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
                token = tokenHeader.substring(7);
            }

            if (token != null && jwtUtils.validateToken(token)) {
                Authentication authentication = jwtUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

//            try {
//                if (token != null && jwtUtils.validateToken(token)) {
//                    Authentication authentication = jwtUtils.getAuthentication(token);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }catch (Exception ex){
//                ObjectMapper objectMapper = new ObjectMapper();
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write(objectMapper.writeValueAsString(
//                        new ExceptionResponse(new Date(), ex.getMessage(), "Access Denied")
//                ));
//            }
        }

        filterChain.doFilter(request, response);
    }

    public class SkipJwtAuthenticationMatcher implements RequestMatcher {
        private OrRequestMatcher skipMatchers;

        private List<RequestMatcher> skipResources;

        public SkipJwtAuthenticationMatcher(List<String> pathsToSkip) {
            skipResources = pathsToSkip.stream()
                    .map(this::getAntPathRequestMatcher)
                    .collect(Collectors.toList());
//            addStaticResources();
            skipMatchers = new OrRequestMatcher(skipResources);
        }

//        private void addStaticResources() {
//             this.skipResources.addAll(
//                    Arrays.asList(PathRequest.toStaticResources().atCommonLocations()));
//        }

        private AntPathRequestMatcher getAntPathRequestMatcher(String info) {
            String[] methodAndPath = info.split(" ");
            String url = info;
            String httpMethod = null;

            if(methodAndPath.length > 1) {
                url = methodAndPath[1];

                if (methodAndPath[0].equals("GET")) {
                    httpMethod = HttpMethod.GET.toString();
                } else if (methodAndPath[0].equals("POST")) {
                    httpMethod = HttpMethod.POST.toString();
                } else if (methodAndPath[0].equals("PATCH")) {
                    httpMethod = HttpMethod.PATCH.toString();
                } else if (methodAndPath[0].equals("PUT")) {
                    httpMethod = HttpMethod.PUT.toString();
                } else if (methodAndPath[0].equals("DELETE")) {
                    httpMethod = HttpMethod.DELETE.toString();
                }

                log.info("Http Method : {}, excluded Url : {}", httpMethod, url);
            }
            return new AntPathRequestMatcher(url, httpMethod);
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return skipMatchers.matches(request);
        }
    }
}

