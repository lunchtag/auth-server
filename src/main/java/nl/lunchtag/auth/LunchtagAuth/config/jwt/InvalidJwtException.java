package nl.lunchtag.auth.LunchtagAuth.config.jwt;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {
    InvalidJwtException(String e) {
        super(e);
    }
}
