package com.example.backend.security.fitler;

import com.example.backend.advice.dto.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        log.info("HTTP Method = {}, Request URL = {}", request.getMethod(),request.getRequestURL());

        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            String message = "Access Denied";

            ExceptionResponse exceptionResponse =
                    new ExceptionResponse(new Date(), e.getMessage(), message);

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(convertObjectToJson(exceptionResponse));

            log.info("Request Access Denied : {} result code : {}", request.getRequestURL(), response.getStatus());
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
