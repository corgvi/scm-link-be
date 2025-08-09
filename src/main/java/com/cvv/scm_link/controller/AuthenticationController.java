package com.cvv.scm_link.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.AuthenticationRequest;
import com.cvv.scm_link.dto.request.IntrospectRequest;
import com.cvv.scm_link.dto.request.LogoutRequest;
import com.cvv.scm_link.dto.request.RefreshRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.AuthenticationResponse;
import com.cvv.scm_link.dto.response.IntrospectResponse;
import com.cvv.scm_link.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public APIResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return APIResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/refresh")
    public APIResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    public APIResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder().build();
    }
}
