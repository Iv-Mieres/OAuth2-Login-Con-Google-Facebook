package com.prueba.login.serviceImp;

import com.prueba.login.dto.response.AuthResponseDto;
import com.prueba.login.model.CustomerUser;
import com.prueba.login.service.ICustomerUserService;
import com.prueba.login.service.IOAuth2LoadUserService;
import com.prueba.login.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuth2LoadUserService extends DefaultOAuth2UserService implements IOAuth2LoadUserService {

    @Autowired
    private ICustomerUserService userService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        return super.loadUser(oAuth2UserRequest);
    }

    @Override
    public AuthResponseDto authSocialLogin(OAuth2AuthorizedClient authorizedClient) {
        String registrationId = authorizedClient.getClientRegistration().getRegistrationId();

        OAuth2UserRequest oauth2UserRequest = new OAuth2UserRequest(
                clientRegistrationRepository.findByRegistrationId(registrationId),
                authorizedClient.getAccessToken());

        var oAuth2User = this.loadUser(oauth2UserRequest);
        var user = userService.saveOAuth2User(oAuth2User, registrationId);

        return new AuthResponseDto(user.getUsername(),
                "Se ha logueado correctamente", this.jwt(user), true);
    }

    private String jwt(CustomerUser user) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        return jwtUtils.createToken(authentication);
    }
}