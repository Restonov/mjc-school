package com.epam.esm.filter;

import com.epam.esm.security.JwtOktaTokenProcessor;
import com.epam.esm.security.JwtTokenProcessor;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.entity.Constants.BEARER_PREFIX;
import static com.epam.esm.entity.Constants.HEADER_STRING;
import static com.epam.esm.entity.Constants.OKTA_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BLANK = "";

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private JwtTokenProcessor tokenProcessor;

    @Autowired
    private JwtOktaTokenProcessor oktaTokenProcessor;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        String userName;
        String authToken;
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            authToken = header.replace(BEARER_PREFIX, BLANK);
            Claims claims;
            try {
                claims = tokenProcessor.decodeToken(authToken);
                userName = tokenProcessor.getUsernameFromClaims(claims);
                if (userName != null) {
                    final UserDetails userDetails = userService.loadUserByUsername(userName);
                    final UsernamePasswordAuthenticationToken authentication = tokenProcessor
                            .generateAuthenticationToken(claims, userDetails);
                    authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                throw new AccessDeniedException("user.token.expired");
            }
        } else if (header != null && header.startsWith(OKTA_PREFIX)) {
            authToken = header.replace(OKTA_PREFIX, BLANK);
            final Jwt jwt;
            try {
                jwt = oktaTokenProcessor.decodeToken(authToken);
                userName = oktaTokenProcessor.getUsernameFromJwt(jwt);
                if (userName != null) {
                    final UserDetails userDetails = userService.loadUserByUsername(userName);
                    final UsernamePasswordAuthenticationToken authentication = oktaTokenProcessor
                            .generateAuthenticationToken(userDetails);
                    authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtVerificationException e) {
                throw new AccessDeniedException("user.token.expired");
            }
        }
        chain.doFilter(request, response);
    }
}