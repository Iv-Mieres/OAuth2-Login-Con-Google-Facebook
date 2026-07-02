package com.prueba.login.controller;

import com.prueba.login.dto.request.AuthLoginDtoReq;
import com.prueba.login.dto.response.AuthResponseDto;
import com.prueba.login.service.IOAuth2LoadUserService;
import com.prueba.login.serviceImp.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final UserDetailsServiceImp userDetailsServiceImp;
    private final IOAuth2LoadUserService oAuth2LoadUserService;

    public AuthenticationController(UserDetailsServiceImp userDetailsServiceImp,
                                    IOAuth2LoadUserService oAuth2LoadUserService) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.oAuth2LoadUserService = oAuth2LoadUserService;
    }

    @PostMapping()
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthLoginDtoReq authLoginDtoReq) {
        return new ResponseEntity<>(userDetailsServiceImp.loginUser(authLoginDtoReq), HttpStatus.OK);
    }

    @GetMapping("/oauth2/code/google")
    public ResponseEntity<AuthResponseDto> googleCallback(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
       return new ResponseEntity<>(oAuth2LoadUserService.authSocialLogin(authorizedClient), HttpStatus.OK);
    }

    @GetMapping("/oauth2/code/facebook")
    public ResponseEntity<AuthResponseDto> facebookCallback(@RegisteredOAuth2AuthorizedClient("facebook") OAuth2AuthorizedClient authorizedClient) {
        return new ResponseEntity<>(oAuth2LoadUserService.authSocialLogin(authorizedClient), HttpStatus.OK);
    }

}
