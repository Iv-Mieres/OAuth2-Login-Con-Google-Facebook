package com.prueba.login.service;

import com.prueba.login.dto.response.AuthResponseDto;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface IOAuth2LoadUserService {

    AuthResponseDto authSocialLogin(OAuth2AuthorizedClient authorizedClient);

}
