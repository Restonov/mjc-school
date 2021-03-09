package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * OKTA JWT decoder, Token generator
 *
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PropertySource({"classpath:security.properties"})
public class JwtOktaTokenProcessor {
    private static final String SUBJECT = "sub";
    private static final String BLANK = "";

    @Value("${okta.issuer}")
    String issuer;

    @Value("${okta.audience}")
    String audience;

    public Jwt decodeToken(String token) throws JwtVerificationException {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setConnectionTimeout(Duration.ofSeconds(INTEGER_ONE))
                .build();
        return jwtVerifier.decode(token);
    }

    @SneakyThrows
    public String getUsernameFromJwt(Jwt jwt) {
        return (String) jwt.getClaims().get(SUBJECT);
    }

    public UsernamePasswordAuthenticationToken generateAuthenticationToken(UserDetails userDetails) {
        final Collection<? extends GrantedAuthority> authorities =
                Stream.of(User.Role.ROLE_USER.name())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, BLANK, authorities);
    }
}
