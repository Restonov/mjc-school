package com.epam.esm.filter;

import com.epam.esm.dto.ExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.entity.Constants.APPLICATION_JSON_UTF8;

public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage((e.getMessage()), new Object[]{}, request.getLocale());
            response.setContentType(APPLICATION_JSON_UTF8);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(
                    new ExceptionDto(
                            errorMessage,
                            HttpStatus.FORBIDDEN.value()
                    ))
            );
        }
    }
}
