package com.prueba.login.config;

import com.prueba.login.config.filter.JwtTokenValidator;
import com.prueba.login.serviceImp.OAuth2LoadUserService;
import com.prueba.login.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class CustomSecurityFilterChain {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private OAuth2LoadUserService customOAuth2UserService;
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    /**
     * Metodo para configurar la seguridad de la aplicacion web, autorizaciones, tipo de sesion, proveedor de autenticacion y filtros
     * @param httpSecurity configuracion de seguridad http
     * @param authenticationManager administrador de autenticacion
     * @return securityFilterChain configuracion de seguridad http
     * @throws Exception mensaje de excepcion si la configuracion falla
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login/**","/oauth2/**").permitAll()
                                .requestMatchers( HttpMethod.GET, "/api/v1/users/**").hasRole("USER")
                                .anyRequest().authenticated())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Client(Customizer.withDefaults())
                .addFilterBefore(new JwtTokenValidator(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }



}
