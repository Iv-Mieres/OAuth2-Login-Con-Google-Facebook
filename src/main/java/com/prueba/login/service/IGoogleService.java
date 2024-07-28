package com.prueba.login.service;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface IGoogleService {

    void loginGoogleUser(OAuth2AuthenticationToken token);

}
