package com.prueba.login.serviceImp;

import com.prueba.login.dto.request.AuthLoginDtoReq;
import com.prueba.login.dto.response.AuthResponseDto;
import com.prueba.login.repository.ICustomerUserRepository;
import com.prueba.login.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private ICustomerUserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no fue encontrado."));
    }

    public AuthResponseDto loginUser(AuthLoginDtoReq authLoginDtoReq){
        Authentication authentication = this.authenticate(authLoginDtoReq.username(), authLoginDtoReq.password());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDto(authLoginDtoReq.username(),
                "Se ha logueado correctamente", accessToken, true );
    }

    public Authentication authenticate (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null){
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

    }


}
