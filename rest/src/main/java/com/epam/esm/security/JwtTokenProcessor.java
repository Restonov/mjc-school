package com.epam.esm.security;

import com.epam.esm.dto.UserFormDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.epam.esm.entity.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.epam.esm.entity.Constants.AUTHORITIES_KEY;
import static com.epam.esm.entity.Constants.SIGNING_KEY;

/**
 * Bearer JWT decoder, Token generator
 *
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProcessor implements Serializable {
    private static final String REGEX_DELIMITER = ",";
    private static final String BLANK = "";
    private final AuthenticationManager authenticationManager;

    public String generateToken(UserFormDto form) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(REGEX_DELIMITER));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .compact();
    }

    public UsernamePasswordAuthenticationToken generateAuthenticationToken(final Claims claims,
                                                                           final UserDetails userDetails) {
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(REGEX_DELIMITER))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, BLANK, authorities);
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromClaims(Claims claims) {
        return claims.getSubject();
    }
}
