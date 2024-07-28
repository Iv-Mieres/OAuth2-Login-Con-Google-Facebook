package com.prueba.login.service;

import com.prueba.login.dto.request.CustomerUserDtoReq;
import com.prueba.login.dto.response.CustomerUserDtoRes;
import com.prueba.login.model.CustomerUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ICustomerUserService {

    CustomerUser saveOAuth2User(OAuth2User oAuth2User, String provider);

    CustomerUserDtoRes saveUser(CustomerUserDtoReq user);
    CustomerUserDtoRes getUserById(Long id);

    CustomerUserDtoRes getUserAuthenticated(String email);

    Page<CustomerUserDtoRes> getAllUsers(Pageable pageable);
    CustomerUserDtoRes updateUserById(CustomerUserDtoReq user, Long id);
    void deleteUserById(Long id);
}
