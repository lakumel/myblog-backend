package com.example.backend.security.fitler;

import com.example.backend.security.dto.LoginDto;
import com.example.backend.security.token.JsonAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JsonAuthenticationFilter(String loginUrl, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(loginUrl));
        this.objectMapper = objectMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if(!isApplicationJSON(request)){
            throw new IllegalStateException("Content Type is not Application/json");
        }

        LoginDto loginDto = null;

        try{
            loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
        }catch (Exception e){
            throw new IllegalArgumentException("Type Exception");
        }

        if(ObjectUtils.isEmpty(loginDto.getEmail()) || ObjectUtils.isEmpty(loginDto.getPassword())){
            throw new IllegalArgumentException("Username or Password is empty");
        }

        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authenticate = this.getAuthenticationManager().authenticate(jsonAuthenticationToken);
        return authenticate;
    }

    private boolean isApplicationJSON(HttpServletRequest httpServletRequest){
        log.info("Request Header : {} ", httpServletRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        log.info("Request Header : {} ", httpServletRequest.getHeader(HttpHeaders.ACCEPT));

        if(httpServletRequest.getHeader(HttpHeaders.CONTENT_TYPE).equals(MediaType.APPLICATION_JSON_VALUE)){
            return true;
        }
        return false;
    }
}
