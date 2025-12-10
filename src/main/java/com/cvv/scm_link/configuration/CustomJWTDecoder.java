package com.cvv.scm_link.configuration;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.cvv.scm_link.dto.request.IntrospectRequest;
import com.cvv.scm_link.dto.response.IntrospectResponse;
import com.cvv.scm_link.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomJWTDecoder implements JwtDecoder {

    @Value("${jwt.signer-key}")
    private String jwtSignerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse introspectResponse = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build());
            if (!introspectResponse.isValid()) throw new JwtException("Invalid token");
        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if (Objects.isNull(jwtDecoder)) {
            SecretKeySpec key = new SecretKeySpec(jwtSignerKey.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(key)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return jwtDecoder.decode(token);
    }
}
